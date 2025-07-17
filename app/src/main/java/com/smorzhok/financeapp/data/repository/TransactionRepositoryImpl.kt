package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.repository.local.TransactionLocalRepository
import com.smorzhok.financeapp.domain.repository.remote.TransactionRemoteRepository
import javax.inject.Inject

/*Имплементация репозитория для данных о транзакциях*/
class TransactionRepositoryImpl @Inject constructor(
    private val remote: TransactionRemoteRepository,
    private val local: TransactionLocalRepository
) : TransactionRepository {

    override suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction> {
        val transactions = remote.getTransactions(accountId, from, to)
        local.saveTransactions(transactions)
        return transactions
    }

    override suspend fun createTransaction(transaction: Transaction) {
        remote.createTransaction(transaction)
    }

    override suspend fun getTransactionById(id: Int): TransactionEdit {
        return remote.getTransactionById(id)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        remote.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(id: Int) {
        remote.deleteTransaction(id)
    }
}
