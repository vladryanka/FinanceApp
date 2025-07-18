package com.smorzhok.financeapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.smorzhok.financeapp.data.worker.SyncWorker
import com.smorzhok.financeapp.di.FinanceApp
import com.smorzhok.financeapp.ui.screen.FinanceRoot
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import jakarta.inject.Inject


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
            FinanceAppTheme {
                FinanceRoot(viewModelFactory)
            }
        }

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            SyncWorker.makeRequest()
        )
    }
}

