package com.smorzhok.financeapp.di

import android.app.Application
import com.smorzhok.financeapp.data.datastore.ThemePreference
import dagger.Module
import dagger.Provides

@Module
object PreferenceModule {

    @Provides
    @AppScope
    fun provideThemePreference(application: Application): ThemePreference {
        return ThemePreference(application)
    }
}