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
    object AddTransaction: Screen(ROUTE_ADD_TRANSACTION)
    object Analytics: Screen(ROUTE_ANALYTICS) {
        fun createRoute(
            isIncome: Boolean,
        ): String {
            return "$route?isIncome=$isIncome"
        }
    }
    object ColorSelection : Screen(ROUTE_COLOR_SELECTION)
    object Haptics : Screen(ROUTE_HAPTICS)
    object PinSetup : Screen(ROUTE_PINSETUP)
    object Info : Screen(ROUTE_INFO)
    object Language : Screen(ROUTE_LANGUAGE)
    object Sync : Screen(ROUTE_SYNC)

    private companion object {
        const val ROUTE_EXPENSES = "expenses"
        const val ROUTE_INCOME = "income"
        const val ROUTE_CHECK = "check"
        const val ROUTE_CATEGORY = "category"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_CHECK_EDITING = "check_editing"
        const val ROUTE_HISTORY = "history/{isIncome}"
        const val ROUTE_ANALYTICS = "analytics"
        const val ROUTE_ADD_TRANSACTION = "add_transaction"
        const val ROUTE_COLOR_SELECTION = "color_selection"
        private const val ROUTE_HAPTICS = "haptics"
        private const val ROUTE_PINSETUP = "pin_setup"
        private const val ROUTE_INFO = "info"
        private const val ROUTE_LANGUAGE = "language"
        private const val ROUTE_SYNC = "sync"
    }
}
