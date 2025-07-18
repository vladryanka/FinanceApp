package com.smorzhok.financeapp.di

import android.app.Application
import androidx.room.Room
import com.smorzhok.financeapp.data.database.FinanceDatabase
import com.smorzhok.financeapp.data.database.dao.AccountDao
import com.smorzhok.financeapp.data.database.dao.CategoryDao
import com.smorzhok.financeapp.data.database.dao.TransactionDao
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @AppScope
    @Provides
    fun provideFinanceDatabase(application: Application): FinanceDatabase {
        return Room.databaseBuilder(
            application,
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