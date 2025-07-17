package com.smorzhok.financeapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.smorzhok.financeapp.data.mapper.toTransactionEdit
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.repository.local.TransactionLocalRepository
import com.smorzhok.financeapp.domain.repository.remote.TransactionRemoteRepository
import java.io.IOException
import javax.inject.Inject

/*Имплементация репозитория для данных о транзакциях*/
class TransactionRepositoryImpl @Inject constructor(
    private val remote: TransactionRemoteRepository,
    private val local: TransactionLocalRepository
) : TransactionRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> {
        return try {
            val transactions = remote.getTransactions(accountId, from, to)
            local.saveTransactions(transactions)
            transactions
        } catch (_: IOException) {
           local.getCachedTransactions(accountId, from, to)
        }
    }

    override suspend fun createTransaction(transaction: Transaction) {
        remote.createTransaction(transaction)
    }

    override suspend fun getTransactionById(id: Int): TransactionEdit {
        return try {
            val transaction = remote.getTransactionById(id)
            local.addTransaction(transaction)
            transaction.toTransactionEdit()
        } catch (_: IOException) {
            local.getTransactionById(id).toTransactionEdit()
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        remote.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(id: Int) {
        remote.deleteTransaction(id)
    }
}
