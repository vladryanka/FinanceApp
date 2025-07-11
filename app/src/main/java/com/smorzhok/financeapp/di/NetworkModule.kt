package com.smorzhok.financeapp.di

import com.smorzhok.financeapp.data.remote.FinanceApi
import com.smorzhok.financeapp.data.remote.FinanceApiService
import dagger.Module
import dagger.Provides

@Module
object NetworkModule {

    @AppScope
    @Provides
    fun provideFinanceApiService(): FinanceApiService {
        return FinanceApi.service
    }
}