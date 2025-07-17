package com.smorzhok.financeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smorzhok.financeapp.data.dao.AccountDao
import com.smorzhok.financeapp.data.dao.CategoryDao
import com.smorzhok.financeapp.data.dao.TransactionDao
import com.smorzhok.financeapp.data.model.entity.AccountEntity
import com.smorzhok.financeapp.data.model.entity.CategoryEntity
import com.smorzhok.financeapp.data.model.entity.TransactionEntity

@Database(entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}