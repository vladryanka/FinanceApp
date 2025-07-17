package com.smorzhok.financeapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        DataModule::class,
        ViewModelModule::class,
        DatabaseModule::class,
        RemoteDataModule:: class,
        LocalDataModule:: class
    ]
)
interface ApplicationComponent {

    fun activityComponent(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}