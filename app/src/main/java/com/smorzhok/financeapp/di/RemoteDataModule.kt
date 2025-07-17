package com.smorzhok.financeapp.di

import com.smorzhok.financeapp.data.repository.remote.AccountRemoteRepositoryImpl
import com.smorzhok.financeapp.data.repository.remote.CategoryRemoteRepositoryImpl
import com.smorzhok.financeapp.data.repository.remote.TransactionRemoteRepositoryImpl
import com.smorzhok.financeapp.domain.repository.remote.AccountRemoteRepository
import com.smorzhok.financeapp.domain.repository.remote.CategoryRemoteRepository
import com.smorzhok.financeapp.domain.repository.remote.TransactionRemoteRepository
import dagger.Binds
import dagger.Module

@Module
interface RemoteDataModule {

    @AppScope
    @Binds
    fun bindTransactionRemoteRepository(
        impl: TransactionRemoteRepositoryImpl
    ): TransactionRemoteRepository

    @AppScope
    @Binds
    fun bindCategoryRemoteRepository(
        impl: CategoryRemoteRepositoryImpl
    ): CategoryRemoteRepository

    @AppScope
    @Binds
    fun bindAccountRemoteRepository(
        impl: AccountRemoteRepositoryImpl
    ): AccountRemoteRepository
}