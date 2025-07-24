package com.smorzhok.financeapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smorzhok.financeapp.data.datastore.PinCodeManager
import com.smorzhok.financeapp.ui.screen.setting.PinSetupScreen
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PinSetupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val pinCodeManager = mockk<PinCodeManager>(relaxed = true)

    @Test
    fun enterMatchingPins_savesPin_andCallsOnPinSet() {
        var onPinSetCalled = false

        composeTestRule.setContent {
            PinSetupScreen(
                paddingValues = PaddingValues(),
                pinCodeManager = pinCodeManager,
                onPinSet = { onPinSetCalled = true }
            )
        }

        val pin = "1234"

        composeTestRule.onNodeWithTag("pinInput").performTextInput(pin)
        composeTestRule.onNodeWithTag("confirmPinInput").performTextInput(pin)
        composeTestRule.onNodeWithText("Continue").performClick()

        coVerify { pinCodeManager.savePin(pin) }
        assert(onPinSetCalled)
    }

    @Test
    fun enterMismatchedPins_showsError() {
        composeTestRule.setContent {
            PinSetupScreen(
                paddingValues = PaddingValues(),
                pinCodeManager = pinCodeManager,
                onPinSet = { }
            )
        }

        composeTestRule.onNodeWithTag("pinInput").performTextInput("1235")
        composeTestRule.onNodeWithTag("confirmPinInput").performTextInput("5678")

        composeTestRule.onNodeWithText("Continue").performClick()

        composeTestRule.onNodeWithText("PIN codes do not match")
            .assertIsDisplayed()
    }
}