package com.smorzhok.financeapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        DataModule::class,
        ViewModelModule::class,
        DatabaseModule::class,
        RemoteDataModule::class,
        LocalDataModule::class,
        WorkerBindingModule:: class,
        PreferenceModule::class
    ]
)
interface ApplicationComponent {

    fun activityComponent(): ActivityComponent.Factory
    fun inject(app: FinanceApp)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}