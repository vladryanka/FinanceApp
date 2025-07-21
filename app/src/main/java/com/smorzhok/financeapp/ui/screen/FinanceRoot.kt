package com.smorzhok.financeapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.ui.screen.setting.ThemeViewModel
import com.smorzhok.financeapp.ui.screen.splash.LottieSplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceRoot(
    viewModelFactory: ViewModelProvider.Factory,
    themeViewModel: ThemeViewModel
) {
    var isSplashFinished by remember { mutableStateOf(false) }

    if (!isSplashFinished) {
        LottieSplashScreen {
            isSplashFinished = true
        }
    } else {
        MainScreen(viewModelFactory, themeViewModel)
    }
}