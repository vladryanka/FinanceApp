package com.smorzhok.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.MainScreen
import com.smorzhok.financeapp.ui.theme.articlesSreen.ArticlesScreenViewModel
import com.smorzhok.financeapp.ui.theme.checkScreen.CheckScreenViewModel
import com.smorzhok.financeapp.ui.theme.expenseScreen.ExpensesScreenViewModel
import com.smorzhok.financeapp.ui.theme.incomeScreen.IncomeScreenViewModel
import com.smorzhok.financeapp.ui.theme.settingScreen.SettingsScreenViewModel

class MainActivity : ComponentActivity() {

    private lateinit var expensesViewModel: ExpensesScreenViewModel
    private lateinit var incomesViewModel: IncomeScreenViewModel
    private lateinit var checksViewModel: CheckScreenViewModel
    private lateinit var articlesViewModel: ArticlesScreenViewModel
    private lateinit var settingsViewModel: SettingsScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        expensesViewModel = ViewModelProvider(this).get(ExpensesScreenViewModel::class.java)
        incomesViewModel = ViewModelProvider(this).get(IncomeScreenViewModel::class.java)
        checksViewModel = ViewModelProvider(this).get(CheckScreenViewModel::class.java)
        articlesViewModel = ViewModelProvider(this).get(ArticlesScreenViewModel::class.java)
        settingsViewModel = ViewModelProvider(this).get(SettingsScreenViewModel::class.java)
        setContent {
            FinanceAppTheme {
                FinanceAppTheme {
                    MainScreen(
                        expensesViewModel = expensesViewModel,
                        incomesViewModel = incomesViewModel,
                        checksViewModel = checksViewModel,
                        articlesViewModel = articlesViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}