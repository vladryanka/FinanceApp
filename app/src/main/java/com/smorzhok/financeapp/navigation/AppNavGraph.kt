package com.smorzhok.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/*навигационный бар с маршрутами и реакциями на клик*/
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    expensesScreenContent: @Composable () -> Unit,
    incomeScreenContent: @Composable () -> Unit,
    checkScreenContent: @Composable () -> Unit,
    checkEditingContent: @Composable () -> Unit,
    categoryScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    historyScreenContent: @Composable (Boolean) -> Unit
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
        composable(Screen.CheckEditing.route) {
            checkEditingContent()
        }
        composable(Screen.Category.route) {
            categoryScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
        composable(
            route = Screen.History.route,
            arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false
            historyScreenContent(isIncome)
        }
    }
}
