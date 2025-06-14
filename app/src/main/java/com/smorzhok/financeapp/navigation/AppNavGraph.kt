package com.smorzhok.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    expensesScreenContent: @Composable () -> Unit,
    incomeScreenContent: @Composable () -> Unit,
    checkScreenContent: @Composable () -> Unit,
    articlesScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    historyScreenContent: @Composable () ->Unit
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) {
            expensesScreenContent()
        }
        composable(Screen.Income.route) {
            incomeScreenContent()
        }
        composable(Screen.Check.route) {
            checkScreenContent()
        }
        composable(Screen.Articles.route) {
            articlesScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
        composable(Screen.History.route) {
            historyScreenContent()
        }
    }
}