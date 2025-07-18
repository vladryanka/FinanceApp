package com.smorzhok.financeapp.di

import android.app.Application
import com.smorzhok.financeapp.BuildConfig
import com.smorzhok.financeapp.data.remote.FinanceApi

/*класс для иниицализации полей для старта приложения*/
class FinanceApp : Application() {
    lateinit var appComponent: ApplicationComponent
        private set
    override fun onCreate() {
        super.onCreate()
        FinanceApi.setAuthToken(BuildConfig.FINANCE_API_KEY)
        appComponent = DaggerApplicationComponent.factory().create(this)
    }
}