package com.smorzhok.financeapp.domain.repository.local

import com.smorzhok.financeapp.domain.model.Transaction

interface TransactionLocalRepository {
    suspend fun saveTransactions(transactions: List<Transaction>)
    suspend fun getCachedTransactions(): List<Transaction>
}