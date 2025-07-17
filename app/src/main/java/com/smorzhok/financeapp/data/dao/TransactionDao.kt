package com.smorzhok.financeapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smorzhok.financeapp.data.model.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND time BETWEEN :from AND :to")
    suspend fun getTransactionsForPeriod(accountId: Int, from: String, to: String): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}