package com.smorzhok.financeapp.data.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smorzhok.financeapp.data.model.colors.AppColorTheme
import com.smorzhok.financeapp.di.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.appDataStore by preferencesDataStore(name = "settings")

object ThemePreferenceKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
}

class ThemePreference @Inject constructor(@AppScope private val context: Context) {

    suspend fun setDarkMode(enabled: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE] = enabled
        }
    }

    val darkModeFlow: Flow<Boolean?> = context.appDataStore.data
        .map { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE]
        }
}

object ColorPreferenceKeys {
    val COLOR_NAME = stringPreferencesKey("main_color_name")
}

class ThemeColorPreference @Inject constructor(
    @AppScope private val application: Application
) {

    suspend fun setMainColor(color: AppColorTheme) {
        application.appDataStore.edit {
            it[ColorPreferenceKeys.COLOR_NAME] = color.name
        }
    }

    val colorFlow: Flow<AppColorTheme?> = application.appDataStore.data
        .map { preferences ->
            preferences[ColorPreferenceKeys.COLOR_NAME]?.let { saved ->
                AppColorTheme.entries.find { it.name == saved }
            }
        }
}
object HapticPreferenceKeys {
    val ENABLED = booleanPreferencesKey("haptic_enabled")
    val EFFECT = stringPreferencesKey("haptic_effect")
}

class HapticPreference @Inject constructor(@AppScope private val context: Context) {

    val hapticFlow: Flow<Pair<Boolean, String?>> = context.appDataStore.data.map { prefs ->
        Pair(
            prefs[HapticPreferenceKeys.ENABLED] ?: false,
            prefs[HapticPreferenceKeys.EFFECT]
        )
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.appDataStore.edit { it[HapticPreferenceKeys.ENABLED] = enabled }
    }

    suspend fun setEffect(effect: String) {
        context.appDataStore.edit { it[HapticPreferenceKeys.EFFECT] = effect }
    }
}