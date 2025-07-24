package com.smorzhok.financeapp.di

import android.app.Application
import androidx.work.Configuration
import com.smorzhok.financeapp.BuildConfig
import com.smorzhok.financeapp.data.datastore.LocaleManager
import com.smorzhok.financeapp.data.datastore.LocalePreference
import com.smorzhok.financeapp.data.worker.DaggerWorkerFactory
import com.smorzhok.network.FinanceApi
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/*класс для иниицализации полей для старта приложения*/
class FinanceApp : Application(), Configuration.Provider {

    lateinit var appComponent: ApplicationComponent
        private set

    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    @Inject
    lateinit var localePreference: LocalePreference

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent.factory().create(this)
        appComponent.inject(this)

        runBlocking {
            val lang = localePreference.getLanguage()
            LocaleManager.setLocale(this@FinanceApp, lang)
        }

        FinanceApi.setAuthToken(BuildConfig.FINANCE_API_KEY)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}