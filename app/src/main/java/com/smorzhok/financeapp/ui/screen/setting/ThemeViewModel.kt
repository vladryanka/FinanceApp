package com.smorzhok.financeapp.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.data.datastore.ThemePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeViewModel @Inject constructor(
    private val themePreference: ThemePreference
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow<Boolean?>(null)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode

    init {
        viewModelScope.launch {
            themePreference.darkModeFlow.collect { savedValue ->
                _isDarkMode.value = savedValue
            }
        }
    }

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch {
            themePreference.setDarkMode(enabled)
            _isDarkMode.value = enabled
        }
    }
}