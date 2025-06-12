package com.smorzhok.financeapp.ui.theme.settingScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Settings

class SettingsScreenViewModel : ViewModel() {
    private val initialSettingsList = mutableListOf<Settings>()
        .apply {
            add(
                Settings(
                    0,
                    textLeadingResId = R.string.main_color,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    1,
                    textLeadingResId = R.string.sounds,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    2,
                    textLeadingResId = R.string.haptics,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    3,
                    textLeadingResId = R.string.password,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    4,
                    textLeadingResId = R.string.synchronizing,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    5,
                    textLeadingResId = R.string.language,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
            add(
                Settings(
                    6,
                    textLeadingResId = R.string.about_the_program,
                    iconTrailingResId = R.drawable.triangle_vert
                )
            )
        }

    private val _settingsList = MutableLiveData<List<Settings>>(initialSettingsList)
    val settingsList: LiveData<List<Settings>> get() = _settingsList

}