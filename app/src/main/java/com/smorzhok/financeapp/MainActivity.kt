package com.smorzhok.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.smorzhok.financeapp.data.repository.RepositoryProvider
import com.smorzhok.financeapp.domain.usecase.FinanceRepository
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.LottieSplashScreen
import com.smorzhok.financeapp.ui.theme.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RepositoryProvider.initialize()
        setContent {
            var isSplashFinished by remember { mutableStateOf(false) }

            val financeRepository = RepositoryProvider.getFinanceRepository()

            CompositionLocalProvider(LocalFinanceRepository provides financeRepository) {
                FinanceAppTheme {
                    if (!isSplashFinished) {
                        LottieSplashScreen {
                            isSplashFinished = true
                        }
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}

val LocalFinanceRepository = staticCompositionLocalOf<FinanceRepository> {
    error("No FinanceRepository provided")
}