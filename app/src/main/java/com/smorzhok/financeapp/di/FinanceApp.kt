package com.smorzhok.financeapp.di

import android.app.Application
import androidx.work.Configuration
import com.smorzhok.financeapp.BuildConfig
import com.smorzhok.financeapp.data.remote.FinanceApi
import com.smorzhok.financeapp.data.worker.DaggerWorkerFactory
import javax.inject.Inject

/*класс для иниицализации полей для старта приложения*/
class FinanceApp : Application(), Configuration.Provider {

    lateinit var appComponent: ApplicationComponent
        private set

    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent.factory().create(this)
        appComponent.inject(this)
        FinanceApi.setAuthToken(BuildConfig.FINANCE_API_KEY)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}