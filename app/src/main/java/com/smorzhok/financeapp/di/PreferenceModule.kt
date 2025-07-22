package com.smorzhok.financeapp.di

import android.app.Application
import com.smorzhok.financeapp.data.datastore.HapticPreference
import com.smorzhok.financeapp.data.datastore.ThemeColorPreference
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

    @Provides
    @AppScope
    fun provideThemeColorPreference(application: Application): ThemeColorPreference {
        return ThemeColorPreference(application)
    }

    @Provides
    @AppScope
    fun provideHapticPreference(application: Application): HapticPreference {
        return HapticPreference(application)
    }
}