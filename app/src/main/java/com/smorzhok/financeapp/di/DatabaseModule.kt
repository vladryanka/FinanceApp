package com.smorzhok.financeapp.di

import android.content.Context
import androidx.room.Room
import com.smorzhok.financeapp.data.dao.AccountDao
import com.smorzhok.financeapp.data.dao.CategoryDao
import com.smorzhok.financeapp.data.dao.TransactionDao
import com.smorzhok.financeapp.data.database.FinanceDatabase
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @AppScope
    @Provides
    fun provideFinanceDatabase(context: Context): FinanceDatabase {
        return Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            "finance_db"
        ).build()
    }

    @AppScope
    @Provides
    fun provideAccountDao(database: FinanceDatabase): AccountDao = database.accountDao()

    @AppScope
    @Provides
    fun provideCategoryDao(database: FinanceDatabase): CategoryDao = database.categoryDao()

    @AppScope
    @Provides
    fun provideTransactionDao(database: FinanceDatabase): TransactionDao = database.transactionDao()
}