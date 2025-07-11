package com.smorzhok.financeapp.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {

    fun inject(activity: MainActivity)

    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: ComponentActivity): ActivityComponent
    }
}