package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit

/*репозиторий для данных о транзакциях*/
interface TransactionRepository {
    suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction>
    suspend fun createTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Int)
    suspend fun getTransactionById(id: Int): TransactionEdit
}
