package com.smorzhok.financeapp.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.smorzhok.financeapp.data.database.FinanceDatabase
import com.smorzhok.financeapp.data.mapper.toAccountUpdateRequest
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toTransactionRequest
import com.smorzhok.network.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncWorker(
    context: Context,
    workerParameters: WorkerParameters,
    database: FinanceDatabase
) : CoroutineWorker(context, workerParameters) {

    private val transactionDao = database.transactionDao()
    private val accountDao = database.accountDao()
    private val api = FinanceApi.service

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SyncWorker", "Started sync work")
            val unsyncedTransactions = transactionDao.getUnsyncedTransactions()
            unsyncedTransactions.forEach { entity ->
                try {
                    val transactionRequest = entity.toDomain().toTransactionRequest()
                    api.createTransaction(transactionRequest)
                    transactionDao.markTransactionAsSynced(entity.id)
                } catch (_: Exception) {
                }
            }

            val unsyncedAccounts = accountDao.getUnsyncedAccounts()
            unsyncedAccounts.forEach { accountEntity ->
                try {
                    val account = accountEntity.toDomain()
                    api.updateAccount(account.id, account.toAccountUpdateRequest())
                    accountDao.markAccountAsSynced(accountEntity.id)
                } catch (_: Exception) {
                }
            }

            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "TransactionAndAccountSyncWorker"

        fun makeRequest(hours: Int): PeriodicWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return PeriodicWorkRequestBuilder<SyncWorker>(hours.toLong(), TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
        }
    }
}