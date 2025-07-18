package com.smorzhok.financeapp.di

import androidx.work.WorkerFactory
import com.smorzhok.financeapp.data.worker.DaggerWorkerFactory
import dagger.Binds
import dagger.Module

@Module
interface WorkerBindingModule {

    @Binds
    fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}