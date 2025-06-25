package com.smorzhok.financeapp.di

import android.app.Application
import com.smorzhok.financeapp.BuildConfig
import com.smorzhok.financeapp.data.remote.FinanceApi
import com.smorzhok.financeapp.data.repository.RepositoryProvider

/*класс для иниицализации полей для старта приложения*/
class FinanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryProvider.initialize()
        FinanceApi.setAuthToken(BuildConfig.FINANCE_API_KEY)
    }
}
