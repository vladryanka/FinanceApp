package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Transaction

/*репозиторий для данных о транзакциях*/
interface TransactionRepository {
    suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction>
    suspend fun createTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Int)
}
