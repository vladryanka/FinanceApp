package com.smorzhok.financeapp.di

import com.smorzhok.financeapp.data.repository.local.AccountLocalRepositoryImpl
import com.smorzhok.financeapp.data.repository.local.CategoryLocalRepositoryImpl
import com.smorzhok.financeapp.data.repository.local.TransactionLocalRepositoryImpl
import com.smorzhok.financeapp.domain.repository.local.AccountLocalRepository
import com.smorzhok.financeapp.domain.repository.local.CategoryLocalRepository
import com.smorzhok.financeapp.domain.repository.local.TransactionLocalRepository
import dagger.Binds
import dagger.Module

@Module
interface LocalDataModule {

    @AppScope
    @Binds
    fun bindAccountLocalRepository(
        impl: AccountLocalRepositoryImpl
    ): AccountLocalRepository

    @AppScope
    @Binds
    fun bindCategoryLocalRepository(
        impl: CategoryLocalRepositoryImpl
    ): CategoryLocalRepository

    @AppScope
    @Binds
    fun bindTransactionLocalRepository(
        impl: TransactionLocalRepositoryImpl
    ): TransactionLocalRepository
}