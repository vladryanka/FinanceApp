package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val api: FinanceApiService
) : TransactionRepository {

    override suspend fun getTransactions(accountId: Int, from: String, to: String): List<Transaction> {
        return api.getTransactionsByAccountAndPeriod(accountId, from, to).map { it.toDomain() }
    }

    override suspend fun createTransaction(transaction: Transaction) {
//        val request = transaction.toCreateRequest()
//        api.createTransaction(request)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
//        val request = transaction.toUpdateRequest()
//        api.updateTransaction(request)
    }

    override suspend fun deleteTransaction(id: Int) {
        //       api.deleteTransaction(DeleteTransactionRequest(id.toString()))
    }
}
