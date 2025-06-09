package com.smorzhok.financeapp.ui.theme


import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smorzhok.financeapp.MainViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.ScaffoldItem
import com.smorzhok.financeapp.navigation.AppNavGraph
import com.smorzhok.financeapp.navigation.Screen
import com.smorzhok.financeapp.navigation.rememberNavigationState

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val navState = rememberNavigationState()
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topBarContent = when (currentRoute) {
        Screen.Expenses.route -> ScaffoldItem(
            textResId = R.string.expenses_today,
            imageResId = R.drawable.refresh
        )

        Screen.Settings.route -> ScaffoldItem(textResId = R.string.settings, imageResId = null)
        Screen.Articles.route -> ScaffoldItem(textResId = R.string.my_articles, imageResId = null)
        Screen.Income.route -> ScaffoldItem(
            textResId = R.string.income_today,
            imageResId = R.drawable.refresh
        )

        Screen.Check.route -> ScaffoldItem(
            textResId = R.string.my_account,
            imageResId = R.drawable.pencil
        )

        else -> null
    }

    Scaffold(
        topBar = {
            topBarContent?.let {
                TopBarTextAndIcon(it.textResId, it.imageResId)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                val items = listOf(
                    NavigationItem.Expenses,
                    NavigationItem.Income,
                    NavigationItem.Check,
                    NavigationItem.Articles,
                    NavigationItem.Settings
                )
                items.forEach { item ->
                    val selected = currentRoute == item.screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navState.navigateTo(item.screen.route)
                        },
                        icon = {
                            Icon(
                                painterResource(item.imageResId),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.titleResId),
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) {
        it

        val expensesList by viewModel.expensesList.observeAsState()
        AppNavGraph(
            navState.navHostController,
            {
                ExpensesScreen(expensesList, it, onExpenseClicked = {})
            },
            {

            },
            {},
            {},
            {}
        )
    }
}