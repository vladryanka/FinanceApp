package com.smorzhok.financeapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: Int,
    val accountId: String,
    val categoryId: Int,
    val categoryName: String,
    val currency: String,
    val categoryEmoji: String,
    val isIncome: Boolean,
    val amount: Double,
    val time: String,
    val date: String,
    val comment: String?,
    val isSynced: Boolean = false
)