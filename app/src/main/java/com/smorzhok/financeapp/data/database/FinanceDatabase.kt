package com.smorzhok.financeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smorzhok.financeapp.data.database.dao.AccountDao
import com.smorzhok.financeapp.data.database.dao.CategoryDao
import com.smorzhok.financeapp.data.database.dao.TransactionDao
import com.smorzhok.financeapp.data.database.entity.AccountEntity
import com.smorzhok.financeapp.data.database.entity.CategoryEntity
import com.smorzhok.financeapp.data.database.entity.TransactionEntity

@Database(entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getInstance(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finance_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}