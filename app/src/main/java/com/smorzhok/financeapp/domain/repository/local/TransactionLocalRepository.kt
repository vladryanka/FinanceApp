package com.smorzhok.financeapp.domain.repository.local

import com.smorzhok.financeapp.domain.model.Transaction

interface TransactionLocalRepository {
    suspend fun saveTransactions(transactions: List<Transaction>)
    suspend fun deleteTransaction(id:Int)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun addTransaction(transaction: Transaction,isSynced: Boolean)
    suspend fun getUnsyncedTransactions(): List<Transaction>
    suspend fun markAsSynced(id: Int)
    suspend fun getTransactionById(id: Int): Transaction
    suspend fun getCachedTransactions(accountId:Int, from: String, to: String): List<Transaction>
}