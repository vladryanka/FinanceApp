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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.AppNavGraph
import com.smorzhok.financeapp.navigation.Screen
import com.smorzhok.financeapp.navigation.rememberNavigationState
import com.smorzhok.financeapp.ui.commonitems.BottomNavigationItem
import com.smorzhok.financeapp.ui.screen.add_transaction.AddTransactionScreen
import com.smorzhok.financeapp.ui.screen.analytics.AnalyticsScreen
import com.smorzhok.financeapp.ui.screen.category.CategoryScreen
import com.smorzhok.financeapp.ui.screen.check.editing.CheckEditingScreen
import com.smorzhok.financeapp.ui.screen.check.main_check.CheckScreen
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon
import com.smorzhok.financeapp.ui.screen.expences.ExpensesScreen
import com.smorzhok.financeapp.ui.screen.history.HistoryScreen
import com.smorzhok.financeapp.ui.screen.incomes.IncomeScreen
import com.smorzhok.financeapp.ui.screen.setting.SettingScreen
import com.smorzhok.financeapp.ui.theme.Green

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModelFactory: ViewModelProvider.Factory) {

    val navState = rememberNavigationState()
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = extractBaseRoute(navBackStackEntry?.destination?.route)

    val currentRouteState = currentRoute

    val topBarContent = when (currentRoute) {
        Screen.Expenses.route -> ScaffoldItem(
            textResId = R.string.expenses_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null,
            backgroundColor = Green
        )

        Screen.Settings.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = null,
            backgroundColor = Green
        )

        Screen.Category.route -> ScaffoldItem(
            textResId = R.string.my_articles, trailingImageResId = null,
            leadingImageResId = null,
            backgroundColor = Green
        )

        Screen.Income.route -> ScaffoldItem(
            textResId = R.string.income_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null,
            backgroundColor = Green
        )

        Screen.Check.route -> ScaffoldItem(
            textResId = R.string.my_account,
            trailingImageResId = R.drawable.pencil,
            leadingImageResId = null,
            backgroundColor = Green
        )

        Screen.History.route -> ScaffoldItem(
            textResId = R.string.my_history,
            trailingImageResId = R.drawable.analysis_icon,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = Green
        )

        Screen.Analytics.route -> ScaffoldItem(
            textResId = R.string.analytics,
            trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        else -> null
    }

    Scaffold(
        topBar = {
            topBarContent?.let {
                when (currentRouteState) {
                    Screen.CheckEditing.route -> {}
                    Screen.AddTransaction.route -> {}
                    else ->
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
                                        navState.navigateTo(
                                            Screen.CheckEditing.route,
                                            usePopUpTo = false
                                        )
                                    }

                                    Screen.History.route -> {
                                        val isIncome = navBackStackEntry?.arguments?.getBoolean("isIncome") ?: false
                                        navState.navigateTo(
                                            Screen.Analytics.createRoute(isIncome),
                                            usePopUpTo = false
                                        )
                                    }
                                }
                            },
                            backgroundColor = it.backgroundColor)
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
                            navState.navigateToRoot(item.screen.route)
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
    ) { paddingValue ->

        AppNavGraph(
            navState.navHostController,
            expensesScreenContent = {
                ExpensesScreen(
                    viewModelFactory,
                    paddingValue,
                    onExpenseClicked = { transactionId ->
                        navState.navigateTo("${Screen.AddTransaction.route}?transactionId=$transactionId")
                    },
                    { navState.navigateTo(Screen.AddTransaction.route) })
            },
            incomeScreenContent = {
                IncomeScreen(
                    viewModelFactory,paddingValue, onIncomeClicked = { transactionId ->
                    navState.navigateTo("${Screen.AddTransaction.route}?transactionId=$transactionId")
                }, {
                    navState.navigateTo(Screen.AddTransaction.route)
                })
            },
            checkScreenContent = {
                CheckScreen(
                    viewModelFactory,paddingValue, onCheckClicked = {}, {})
            },
            checkEditingContent = {
                CheckEditingScreen(
                    viewModelFactory,navState)
            },
            categoryScreenContent = {
                CategoryScreen(
                    viewModelFactory,paddingValue, onCategoryClicked = {})
            },
            settingsScreenContent = {
                SettingScreen(
                    paddingValue, onSettingClicked = {})
            },
            historyScreenContent = { isIncome ->
                HistoryScreen(
                    viewModelFactory,isIncome, onHistoryItemClicked = {}, paddingValue)
            },
            addTransactionContent = {
                transactionId ->
                AddTransactionScreen(
                    viewModelFactory,navState, transactionId)
            },
            analyticsScreenContent = { isIncome ->
                AnalyticsScreen(
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValue,
                    isIncome = isIncome
                )
            }
        )
    }
}
private fun extractBaseRoute(route: String?): String? {
    return route?.substringBefore("?")
}