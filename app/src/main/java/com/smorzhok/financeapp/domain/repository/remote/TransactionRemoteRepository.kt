package com.smorzhok.financeapp.domain.repository.remote

import com.smorzhok.financeapp.domain.model.Transaction
import retrofit2.Response

interface TransactionRemoteRepository {
    suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction>
    suspend fun createTransaction(transaction: Transaction)
    suspend fun getTransactionById(id: Int): Transaction
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Int): Response<Unit>
}