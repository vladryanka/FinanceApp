package com.smorzhok.financeapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val isSynced: Boolean = false
)

