package com.smorzhok.financeapp.data.repository.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.smorzhok.financeapp.data.database.dao.TransactionDao
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toEntity
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.local.TransactionLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionLocalRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionLocalRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveTransactions(transactions: List<Transaction>) =
        withContext(Dispatchers.IO) {
            dao.insertTransactionList(transactions.map { it.toEntity() })
        }

    override suspend fun deleteTransaction(id: Int) {
        withContext(Dispatchers.IO) {
            dao.deleteTransaction(id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            dao.updateTransaction(transaction.toEntity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addTransaction(transaction: Transaction,isSynced: Boolean) = withContext(Dispatchers.IO) {
        dao.insertTransaction(transaction.toEntity().copy(isSynced = isSynced))
    }

    override suspend fun getTransactionById(id: Int): Transaction = withContext(Dispatchers.IO) {
        dao.getTransactionById(id).toDomain()
    }

    override suspend fun getUnsyncedTransactions(): List<Transaction> {
        return dao.getUnsyncedTransactions().map { it.toDomain() }
    }

    override suspend fun markAsSynced(id: Int) {
        dao.markTransactionAsSynced(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCachedTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> =
        withContext(Dispatchers.IO) {
            dao.getTransactionsForPeriod(accountId, from, to).map { it.toDomain() }
        }

}