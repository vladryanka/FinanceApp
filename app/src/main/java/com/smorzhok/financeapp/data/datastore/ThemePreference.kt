package com.smorzhok.financeapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferenceKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
}

class ThemePreference(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE] = enabled
        }
    }

    val darkModeFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[ThemePreferenceKeys.DARK_MODE]
        }
}
