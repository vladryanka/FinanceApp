package com.smorzhok.financeapp.ui.commonitems

import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.navigation.Screen

/*инкапсулирует данные, необходимые для отображения навигационных айтемов*/
sealed class BottomNavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val trailingImageResId: Int,
) {
    object Expenses : BottomNavigationItem(
        Screen.Expenses,
        R.string.expenses,
        R.drawable.expenses
    )

    object Income : BottomNavigationItem(
        Screen.Income,
        R.string.income,
        R.drawable.income
    )

    object Check : BottomNavigationItem(
        Screen.Check,
        R.string.check,
        R.drawable.check
    )

    object Category : BottomNavigationItem(
        Screen.Category,
        R.string.articles,
        R.drawable.articles
    )

    object Settings : BottomNavigationItem(
        Screen.Settings,
        R.string.settings,
        R.drawable.settings
    )
}