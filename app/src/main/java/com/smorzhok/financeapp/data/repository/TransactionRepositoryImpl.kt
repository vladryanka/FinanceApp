package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toTransactionEdit
import com.smorzhok.financeapp.data.mapper.toTransactionRequest
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.data.retryWithBackoff
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*Имплементация репозитория для данных о транзакциях*/
class TransactionRepositoryImpl @Inject constructor(
    private val api: FinanceApiService
) : TransactionRepository {

    override suspend fun getTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> = withContext(Dispatchers.IO) {
        retryWithBackoff {
            api.getTransactionsByAccountAndPeriod(accountId, from, to)
                .map { it.toDomain() }
                .ifEmpty { emptyList() }
        }
    }

    override suspend fun createTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            val req = transaction.toTransactionRequest()
            api.createTransaction(req)
        }
    }

    override suspend fun getTransactionById(id: Int): TransactionEdit =
        withContext(Dispatchers.IO) {
            api.getTransactionsById(id).toTransactionEdit()
        }

    override suspend fun updateTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            val request = transaction.toTransactionRequest()
            api.updateTransaction(transaction.id, request)
        }
    }

    override suspend fun deleteTransaction(id: Int) {
        withContext(Dispatchers.IO) {
            api.deleteTransaction(id)
        }
    }

}
