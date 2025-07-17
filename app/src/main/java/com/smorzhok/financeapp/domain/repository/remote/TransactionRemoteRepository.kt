package com.smorzhok.financeapp.domain.repository.remote

import com.smorzhok.financeapp.data.model.dto.transaction.TransactionDto
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionResponse
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit
import retrofit2.Response

interface TransactionRemoteRepository {
    suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction>
    suspend fun createTransaction(transaction: Transaction): Response<TransactionResponse>
    suspend fun getTransactionById(id: Int): TransactionEdit
    suspend fun updateTransaction(transaction: Transaction): Response<TransactionDto>
    suspend fun deleteTransaction(id: Int): Response<Unit>
}