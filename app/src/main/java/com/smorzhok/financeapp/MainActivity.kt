package com.smorzhok.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.data.repository.RepositoryProvider
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.LottieSplashScreen
import com.smorzhok.financeapp.ui.theme.MainScreen
import com.smorzhok.financeapp.ui.theme.categoryScreen.CategoryScreenViewModel
import com.smorzhok.financeapp.ui.theme.categoryScreen.CategoryScreenViewModelFactory
import com.smorzhok.financeapp.ui.theme.checkScreen.CheckScreenViewModel
import com.smorzhok.financeapp.ui.theme.checkScreen.CheckScreenViewModelFactory
import com.smorzhok.financeapp.ui.theme.expenseScreen.ExpensesScreenViewModel
import com.smorzhok.financeapp.ui.theme.expenseScreen.ExpensesScreenViewModelFactory
import com.smorzhok.financeapp.ui.theme.incomeScreen.IncomeScreenViewModel
import com.smorzhok.financeapp.ui.theme.incomeScreen.IncomeScreenViewModelFactory
import com.smorzhok.financeapp.ui.theme.settingScreen.SettingsScreenViewModel

class MainActivity : ComponentActivity() {

    private lateinit var expensesViewModel: ExpensesScreenViewModel
    private lateinit var incomesViewModel: IncomeScreenViewModel
    private lateinit var checksViewModel: CheckScreenViewModel
    private lateinit var categoryViewModel: CategoryScreenViewModel
    private lateinit var settingsViewModel: SettingsScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RepositoryProvider.initialize()
        val financeRepository = RepositoryProvider.getFinanceRepository()

        val expensesFactory = ExpensesScreenViewModelFactory(financeRepository)
        expensesViewModel = ViewModelProvider(this, expensesFactory)[ExpensesScreenViewModel::class.java]

        val incomesFactory = IncomeScreenViewModelFactory(financeRepository)
        incomesViewModel = ViewModelProvider(this, incomesFactory)[IncomeScreenViewModel::class.java]

        val checksFactory = CheckScreenViewModelFactory(financeRepository)
        checksViewModel = ViewModelProvider(this, checksFactory)[CheckScreenViewModel::class.java]

        val categoryFactory = CategoryScreenViewModelFactory(financeRepository)
        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryScreenViewModel::class.java]

        settingsViewModel = ViewModelProvider(this).get(SettingsScreenViewModel::class.java)
        setContent {
            var isSplashFinished by remember { mutableStateOf(false) }

            FinanceAppTheme {
                if (!isSplashFinished) {
                    LottieSplashScreen {
                        isSplashFinished = true
                    }
                } else {
                    MainScreen(
                        expensesViewModel = expensesViewModel,
                        incomesViewModel = incomesViewModel,
                        checksViewModel = checksViewModel,
                        categoryViewModel = categoryViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}