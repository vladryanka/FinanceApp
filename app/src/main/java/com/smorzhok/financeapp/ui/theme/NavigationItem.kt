package com.smorzhok.financeapp.ui.theme

import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val imageResId: Int
) {
    object Expenses: NavigationItem(
        Screen.Expenses,
        R.string.expenses,
        R.drawable.expenses
    )
    object Income: NavigationItem(
        Screen.Income,
        R.string.income,
        R.drawable.income
    )
    object Check: NavigationItem(
        Screen.Check,
        R.string.check,
        R.drawable.check
    )
    object Articles: NavigationItem(
        Screen.Articles,
        R.string.articles,
        R.drawable.articles
    )
    object Settings: NavigationItem(
        Screen.Settings,
        R.string.settings,
        R.drawable.settings
    )
}