package com.smorzhok.financeapp.di

import com.smorzhok.financeapp.data.repository.AccountRepositoryImpl
import com.smorzhok.financeapp.data.repository.CategoryRepositoryImpl
import com.smorzhok.financeapp.data.repository.TransactionRepositoryImpl
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.CategoryRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @AppScope
    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @AppScope
    @Binds
    fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @AppScope
    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

}