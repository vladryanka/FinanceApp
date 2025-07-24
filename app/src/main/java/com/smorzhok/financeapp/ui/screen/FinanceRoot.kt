package com.smorzhok.financeapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.data.datastore.PinCodeManager
import com.smorzhok.financeapp.ui.commonitems.ThemeViewModel
import com.smorzhok.financeapp.ui.screen.setting.PinEntryScreen
import com.smorzhok.financeapp.ui.screen.splash.LottieSplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceRoot(
    viewModelFactory: ViewModelProvider.Factory,
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
    val pinCodeManager = remember { PinCodeManager(context) }
    var isPinChecked by rememberSaveable { mutableStateOf(false) }
    var isPinCorrect by rememberSaveable { mutableStateOf(false) }
    var isSplashFinished by rememberSaveable { mutableStateOf(false) }

    if (!isSplashFinished) {
        LottieSplashScreen {
            isSplashFinished = true
        }
    } else {
        when {
            !isPinChecked && pinCodeManager.isPinSet() -> {
                PinEntryScreen(
                    onSuccess = {
                        isPinCorrect = true
                        isPinChecked = true
                    },
                    pinCodeManager = pinCodeManager
                )
            }

            !pinCodeManager.isPinSet() || isPinCorrect -> {
                MainScreen(viewModelFactory, themeViewModel)
            }
        }
    }

}