package com.smorzhok.financeapp.ui.commonitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.data.datastore.ThemeColorPreference
import com.smorzhok.financeapp.data.datastore.ThemePreference
import com.smorzhok.financeapp.data.model.colors.AppColorTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeViewModel @Inject constructor(
    private val themePreference: ThemePreference,
    private val colorPreference: ThemeColorPreference
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow<Boolean?>(null)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode

    private val _appColor = MutableStateFlow<AppColorTheme>(AppColorTheme.GREEN)
    val appColor: StateFlow<AppColorTheme> = _appColor

    init {
        viewModelScope.launch {
            themePreference.darkModeFlow.collect {
                _isDarkMode.value = it
            }
        }
        viewModelScope.launch {
            colorPreference.colorFlow.collect {
                _appColor.value = it ?: AppColorTheme.GREEN
            }
        }
    }

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch {
            themePreference.setDarkMode(enabled)
            _isDarkMode.value = enabled
        }
    }

    fun setMainColor(color: AppColorTheme) {
        viewModelScope.launch {
            colorPreference.setMainColor(color)
            _appColor.value = color
        }
    }
}