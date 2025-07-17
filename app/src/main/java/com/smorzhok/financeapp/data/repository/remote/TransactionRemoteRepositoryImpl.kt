package com.smorzhok.financeapp.data.repository.remote

import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toTransactionRequest
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.data.retryWithBackoff
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.remote.TransactionRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRemoteRepositoryImpl @Inject constructor(
    private val api: FinanceApiService
) : TransactionRemoteRepository {

    override suspend fun getTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> =
        withContext(Dispatchers.IO) {
            retryWithBackoff {
                api.getTransactionsByAccountAndPeriod(accountId, from, to)
                    .map { it.toDomain() }
            }
        }

    override suspend fun createTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            api.createTransaction(transaction.toTransactionRequest())
        }
    }

    override suspend fun getTransactionById(id: Int): Transaction{
        return withContext(Dispatchers.IO) {
            api.getTransactionsById(id).toDomain()
        }
    }


    override suspend fun updateTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            api.updateTransaction(transaction.id, transaction.toTransactionRequest())
        }
    }


    override suspend fun deleteTransaction(id: Int) = withContext(Dispatchers.IO) {
        api.deleteTransaction(id)
    }
}