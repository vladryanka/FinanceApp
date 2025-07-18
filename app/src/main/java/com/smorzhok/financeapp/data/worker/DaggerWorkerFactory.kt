package com.smorzhok.financeapp.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.smorzhok.financeapp.data.database.FinanceDatabase
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
    private val database: FinanceDatabase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> {
                SyncWorker(appContext, workerParameters, database)
            }
            else -> null
        }
    }
}
