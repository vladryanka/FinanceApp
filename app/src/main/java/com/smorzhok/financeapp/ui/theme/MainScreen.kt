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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.AppNavGraph
import com.smorzhok.financeapp.navigation.Screen
import com.smorzhok.financeapp.navigation.rememberNavigationState
import com.smorzhok.financeapp.ui.theme.categoryScreen.CategoryScreen
import com.smorzhok.financeapp.ui.theme.checkScreen.ChecksScreen
import com.smorzhok.financeapp.ui.theme.commonItems.BottomNavigationItem
import com.smorzhok.financeapp.ui.theme.commonItems.TopBarTextAndIcon
import com.smorzhok.financeapp.ui.theme.expenseScreen.ExpensesScreen
import com.smorzhok.financeapp.ui.theme.historyScreen.HistoryScreen
import com.smorzhok.financeapp.ui.theme.incomeScreen.IncomeScreen
import com.smorzhok.financeapp.ui.theme.settingScreen.SettingScreen

@Composable
fun MainScreen() {

    val navState = rememberNavigationState()
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topBarContent = when (currentRoute) {
        Screen.Expenses.route -> ScaffoldItem(
            textResId = R.string.expenses_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null
        )

        Screen.Settings.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = null
        )

        Screen.Articles.route -> ScaffoldItem(
            textResId = R.string.my_articles, trailingImageResId = null,
            leadingImageResId = null
        )

        Screen.Income.route -> ScaffoldItem(
            textResId = R.string.income_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null
        )

        Screen.Check.route -> ScaffoldItem(
            textResId = R.string.my_account,
            trailingImageResId = R.drawable.pencil,
            leadingImageResId = null
        )

        Screen.History.route -> ScaffoldItem(
            textResId = R.string.my_history,
            trailingImageResId = R.drawable.analysis_icon,
            leadingImageResId = R.drawable.back_icon
        )

        else -> null
    }

    Scaffold(
        topBar = {
            topBarContent?.let {
                TopBarTextAndIcon(
                    it.textResId, trailingImageResId = it.trailingImageResId,
                    leadingImageResId = it.leadingImageResId, onLeadingClicked = {

                    }, onTrailingClicked = {
                        when (currentRoute) {
                            Screen.Expenses.route ->{
                                navState.navigateTo(Screen.History.route)
                            }
                            Screen.Income.route ->{}
                            Screen.Check.route -> {
                                navState.navigateTo(Screen.History.route)
                            }
                            Screen.History.route -> {
                                // Дополнительные действия для анализа
                            }
                        }

                    })
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                val items = listOf(
                    BottomNavigationItem.Expenses,
                    BottomNavigationItem.Income,
                    BottomNavigationItem.Check,
                    BottomNavigationItem.Articles,
                    BottomNavigationItem.Settings
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
                                painterResource(item.trailingImageResId),
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

        AppNavGraph(
            navState.navHostController,
            {
                ExpensesScreen(it, onExpenseClicked = {}, {})
            },
            {
                IncomeScreen(it, onIncomeClicked = {}, {})
            },
            {
                ChecksScreen(it, onCheckClicked = {}, {})
            },
            {
                CategoryScreen(it, onArticleClicked = {})
            },
            {
                SettingScreen(it, onSettingClicked = {})
            },
            {
                HistoryScreen(onHistoryItemClicked = {}, it)
            }
        )
    }
}