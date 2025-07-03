package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toTransactionRequest
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.TransactionRepository

/*Имплементация репозитория для данных о транзакциях*/
class TransactionRepositoryImpl(
    private val api: FinanceApiService
) : TransactionRepository {

    private var _currency = "RUB"

    override suspend fun getTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> {
        val response = api.getTransactionsByAccountAndPeriod(accountId, from, to).map { it.toDomain() }
        _currency = response[0].currency
        return response
    }

    override suspend fun createTransaction(transaction: Transaction) {
//        val request = transaction.toCreateRequest()
//        api.createTransaction(request)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val request = transaction.toTransactionRequest()
        api.updateTransaction(transaction.id, request)
        _currency = transaction.currency
    }

    override fun getCurrentCurrency() = _currency

    override suspend fun deleteTransaction(id: Int) {
        //       api.deleteTransaction(DeleteTransactionRequest(id.toString()))
    }
}
