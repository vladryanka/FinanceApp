package com.smorzhok.financeapp.data.repository.local

import com.smorzhok.financeapp.data.dao.TransactionDao
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toEntity
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.local.TransactionLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionLocalRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionLocalRepository {

    override suspend fun saveTransactions(transactions: List<Transaction>) = withContext(Dispatchers.IO) {
        dao.insertTransactions(transactions.map { it.toEntity() })
    }

    override suspend fun getCachedTransactions(): List<Transaction> = withContext(Dispatchers.IO) {
        dao.getAllTransactions().map { it.toDomain() }
    }
}