package com.smorzhok.financeapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.smorzhok.financeapp.data.repository.RepositoryProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.CategoryRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.ui.screen.splash.LottieSplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceRoot() {
    var isSplashFinished by remember { mutableStateOf(false) }

    val accountRepository = RepositoryProvider.getAccountRepository()
    val categoryRepository = RepositoryProvider.getCategoryRepository()
    val transactionRepository = RepositoryProvider.getTransactionRepository()

    CompositionLocalProvider(
        LocalTransactionRepository provides transactionRepository,
        LocalAccountRepository provides accountRepository,
        LocalCategoryRepository provides categoryRepository,
    ) {
        if (!isSplashFinished) {
            LottieSplashScreen {
                isSplashFinished = true
            }
        } else {
            MainScreen()
        }
    }
}

val LocalAccountRepository = staticCompositionLocalOf<AccountRepository> {
    error("No AccountRepository provided")
}
val LocalTransactionRepository = staticCompositionLocalOf<TransactionRepository> {
    error("No TransactionRepository provided")
}
val LocalCategoryRepository = staticCompositionLocalOf<CategoryRepository> {
    error("No CategoryRepository provided")
}