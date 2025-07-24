package com.smorzhok.financeapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.smorzhok.financeapp.ui.screen.setting.SettingScreen
import org.junit.Rule
import org.junit.Test

class SettingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun switchToggle_changesThemeState() {
        val isDarkThemeState = mutableStateOf(false)

        composeTestRule.setContent {
            SettingScreen(
                hapticEffectType = "CLICK",
                paddingValues = PaddingValues(),
                onSettingClicked = {},
                isDarkTheme = isDarkThemeState.value,
                onToggleDarkMode = { enabled ->
                    isDarkThemeState.value = enabled
                }
            )
        }

        composeTestRule.onNodeWithTag("darkThemeSwitch").assertIsOff()
        composeTestRule.onNodeWithTag("darkThemeSwitch").performClick()
        composeTestRule.onNodeWithTag("darkThemeSwitch").assertIsOn()
    }
}