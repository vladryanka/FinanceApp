package com.smorzhok.financeapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smorzhok.financeapp.data.model.colors.AppColorTheme
import com.smorzhok.financeapp.ui.screen.setting.ColorSelectionScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ColorSelectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectColor_changesSelectedColor() {
        var selectedColor: AppColorTheme = AppColorTheme.BLUE

        composeTestRule.setContent {
            ColorSelectionScreen(
                hapticEffectType = "",
                padding = PaddingValues(),
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it }
            )
        }

        composeTestRule.onNodeWithText(AppColorTheme.BLUE.name)
            .performClick()

        assert(selectedColor == AppColorTheme.BLUE)
    }
}