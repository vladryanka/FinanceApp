package com.smorzhok.financeapp.di

import com.smorzhok.network.FinanceApi
import com.smorzhok.network.FinanceApiService
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