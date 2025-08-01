package com.smorzhok.financeapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.smorzhok.financeapp.data.datastore.LocaleManager
import com.smorzhok.financeapp.data.datastore.LocalePreference
import com.smorzhok.financeapp.data.worker.SyncWorker
import com.smorzhok.financeapp.di.FinanceApp
import com.smorzhok.financeapp.ui.screen.FinanceRoot
import com.smorzhok.financeapp.ui.commonitems.ThemeViewModel
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appComponent = (application as FinanceApp).appComponent
        val activityComponent = appComponent.activityComponent().create(this)
        activityComponent.inject(this)

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = viewModelFactory)
            val isDarkTheme = themeViewModel.isDarkMode.collectAsState().value
            val systemTheme = isSystemInDarkTheme()
            val colorTheme = themeViewModel.appColor.collectAsState().value
            val useDark = isDarkTheme ?: systemTheme
            FinanceAppTheme(darkTheme = useDark, colorTheme = colorTheme) {
                FinanceRoot(viewModelFactory, themeViewModel)
            }
        }

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            SyncWorker.makeRequest(1)
        )
    }
    override fun attachBaseContext(newBase: Context) {
        val localeCode = runBlocking {
            LocalePreference(newBase).getLanguage()
        }
        val context = LocaleManager.setLocale(newBase, localeCode)
        super.attachBaseContext(context)
    }

}


