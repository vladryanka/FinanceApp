package com.smorzhok.financeapp.ui.screen


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.AppNavGraph
import com.smorzhok.financeapp.navigation.Screen
import com.smorzhok.financeapp.navigation.rememberNavigationState
import com.smorzhok.financeapp.ui.commonitems.BottomNavigationItem
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {

    val navState = rememberNavigationState()
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentRouteState by remember(currentRoute) { mutableStateOf(currentRoute) }

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

        Screen.Category.route -> ScaffoldItem(
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
                if (currentRouteState != Screen.CheckEditing.route) {
                    TopBarTextAndIcon(
                        it.textResId, trailingImageResId = it.trailingImageResId,
                        leadingImageResId = it.leadingImageResId, onLeadingClicked = {
                            navState.navHostController.popBackStack()
                        }, onTrailingClicked = {

                            when (currentRoute) {
                                Screen.Expenses.route -> {
                                    navState.navigateTo(
                                        Screen.History.createRoute(false),
                                        usePopUpTo = false
                                    )
                                }

                                Screen.Income.route -> {
                                    navState.navigateTo(
                                        Screen.History.createRoute(true),
                                        usePopUpTo = false
                                    )
                                }

                                Screen.Check.route -> {
                                    navState.navigateTo(Screen.CheckEditing.route, usePopUpTo = false)
                                }

                                Screen.History.route -> {}
                            }

                        })
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                val items = listOf(
                    BottomNavigationItem.Expenses,
                    BottomNavigationItem.Income,
                    BottomNavigationItem.Check,
                    BottomNavigationItem.Category,
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
    ) {paddingValue ->

        AppNavGraph(
            navState.navHostController,
            expensesScreenContent = {
                ExpensesScreen(paddingValue, onExpenseClicked = {}, {})
            },
            incomeScreenContent = {
                IncomeScreen(paddingValue, onIncomeClicked = {}, {})
            },
            checkScreenContent = {
                CheckScreen(paddingValue, onCheckClicked = {}, {})
            },
            checkEditingContent = {
                CheckEditingScreen(navState)
            },
            categoryScreenContent = {
                CategoryScreen(paddingValue, onCategoryClicked = {})
            },
            settingsScreenContent = {
                SettingScreen(paddingValue, onSettingClicked = {})
            },
            historyScreenContent = { isIncome ->
                HistoryScreen(isIncome, onHistoryItemClicked = {}, paddingValue)
            }
        )
    }
}