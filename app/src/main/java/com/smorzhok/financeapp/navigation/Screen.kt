package com.smorzhok.financeapp.navigation

/*централизованное описание всех экранов с их маршрутами*/
sealed class Screen(val route: String) {
    object Expenses : Screen(ROUTE_EXPENSES)
    object Income : Screen(ROUTE_INCOME)
    object Check : Screen(ROUTE_CHECK)
    object Category : Screen(ROUTE_CATEGORY)
    object Settings : Screen(ROUTE_SETTINGS)
    object CheckEditing : Screen(ROUTE_CHECK_EDITING)
    object History : Screen(ROUTE_HISTORY) {
        fun createRoute(isIncome: Boolean) = "history/$isIncome"
    }

    private companion object {
        const val ROUTE_EXPENSES = "expenses"
        const val ROUTE_INCOME = "income"
        const val ROUTE_CHECK = "check"
        const val ROUTE_CATEGORY = "category"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_CHECK_EDITING = "check_editing"
        const val ROUTE_HISTORY = "history/{isIncome}"
    }
}
