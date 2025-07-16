package com.smorzhok.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/*управление переходами между экранами через NavHostController*/
class NavigationState(
    val navHostController: NavHostController,
) {
    fun navigateTo(route: String, usePopUpTo: Boolean = true) {
        navHostController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            if (usePopUpTo) {
                popUpTo(navHostController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
    fun navigateToRoot(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.startDestinationId) {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

/*Создаёт и запоминает экземпляр NavigationState для управления навигацией в Compose*/
@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}
