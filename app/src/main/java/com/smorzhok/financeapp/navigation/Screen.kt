package com.smorzhok.financeapp.navigation

sealed class Screen(val route: String) {
    object Expenses: Screen(ROUTE_EXPENSES)
    object Income: Screen(ROUTE_INCOME)
    object Check: Screen(ROUTE_CHECK)
    object Articles: Screen(ROUTE_ARTICLES)
    object Settings: Screen(ROUTE_SETTINGS)
    object History: Screen(ROUTE_HISTORY) {
        fun createRoute(isIncome: Boolean) = "history/$isIncome"
    }
    private companion object{
        const val ROUTE_EXPENSES = "expenses"
        const val ROUTE_INCOME = "income"
        const val ROUTE_CHECK = "check"
        const val ROUTE_ARTICLES = "articles"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_HISTORY = "history/{isIncome}"
    }
}