package com.smorzhok.financeapp.ui.screen


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.data.datastore.LocalePreference
import com.smorzhok.financeapp.data.datastore.PinCodeManager
import com.smorzhok.financeapp.data.datastore.SyncPreference
import com.smorzhok.financeapp.data.worker.SyncWorker
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.AppNavGraph
import com.smorzhok.financeapp.navigation.Screen
import com.smorzhok.financeapp.navigation.rememberNavigationState
import com.smorzhok.financeapp.ui.commonitems.BottomNavigationItem
import com.smorzhok.financeapp.ui.commonitems.HapticViewModel
import com.smorzhok.financeapp.ui.commonitems.ThemeViewModel
import com.smorzhok.financeapp.ui.screen.add_transaction.AddTransactionScreen
import com.smorzhok.financeapp.ui.screen.analytics.AnalyticsScreen
import com.smorzhok.financeapp.ui.screen.category.CategoryScreen
import com.smorzhok.financeapp.ui.screen.check.editing.CheckEditingScreen
import com.smorzhok.financeapp.ui.screen.check.main_check.CheckScreen
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon
import com.smorzhok.financeapp.ui.screen.expences.ExpensesScreen
import com.smorzhok.financeapp.ui.screen.history.HistoryScreen
import com.smorzhok.financeapp.ui.screen.incomes.IncomeScreen
import com.smorzhok.financeapp.ui.screen.setting.ColorSelectionScreen
import com.smorzhok.financeapp.ui.screen.setting.HapticScreen
import com.smorzhok.financeapp.ui.screen.setting.InfoScreen
import com.smorzhok.financeapp.ui.screen.setting.LanguageScreen
import com.smorzhok.financeapp.ui.screen.setting.PinSetupScreen
import com.smorzhok.financeapp.ui.screen.setting.SettingScreen
import com.smorzhok.financeapp.ui.screen.setting.SyncScreen
import com.smorzhok.financeapp.ui.screen.setting.performHapticFeedback

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModelFactory: ViewModelProvider.Factory,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme = themeViewModel.isDarkMode.collectAsState().value ?: isSystemInDarkTheme()

    val navState = rememberNavigationState()
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = extractBaseRoute(navBackStackEntry?.destination?.route)

    val currentRouteState = currentRoute
    val settingsViewModel: HapticViewModel = viewModel(factory = viewModelFactory)
    val hapticEffectType by settingsViewModel.hapticEffect.collectAsState()
    val context = LocalContext.current
    val pinCodeManager = remember { PinCodeManager(context) }
    val localePreference = remember { LocalePreference(context) }
    var currentLanguage by remember { mutableStateOf("ru") }
    LaunchedEffect(Unit) {
        currentLanguage = localePreference.getLanguage()
    }

    val topBarContent = when (currentRoute) {
        Screen.Expenses.route -> ScaffoldItem(
            textResId = R.string.expenses_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Settings.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = null,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.ColorSelection.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.PinSetup.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Info.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Haptics.route -> ScaffoldItem(
            textResId = R.string.settings, trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Category.route -> ScaffoldItem(
            textResId = R.string.my_articles, trailingImageResId = null,
            leadingImageResId = null,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Income.route -> ScaffoldItem(
            textResId = R.string.income_today,
            trailingImageResId = R.drawable.refresh,
            leadingImageResId = null,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Check.route -> ScaffoldItem(
            textResId = R.string.my_account,
            trailingImageResId = R.drawable.pencil,
            leadingImageResId = null,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.History.route -> ScaffoldItem(
            textResId = R.string.my_history,
            trailingImageResId = R.drawable.analysis_icon,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )

        Screen.Analytics.route -> ScaffoldItem(
            textResId = R.string.analytics,
            trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.surface
        )

        Screen.Sync.route -> ScaffoldItem(
            textResId = R.string.settings,
            trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
        )
        Screen.Language.route -> ScaffoldItem(
            textResId = R.string.settings,
            trailingImageResId = null,
            leadingImageResId = R.drawable.back_icon,
            backgroundColor = MaterialTheme.colorScheme.primary
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
                                        val isIncome =
                                            navBackStackEntry?.arguments?.getBoolean("isIncome") == true
                                        navState.navigateTo(
                                            Screen.Analytics.createRoute(isIncome),
                                            usePopUpTo = false
                                        )
                                    }
                                }
                            },
                            backgroundColor = it.backgroundColor
                        )
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
                            performHapticFeedback(context = context, effect = hapticEffectType)
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
                    viewModelFactory, paddingValue, onIncomeClicked = { transactionId ->
                        navState.navigateTo("${Screen.AddTransaction.route}?transactionId=$transactionId")
                    }, {
                        navState.navigateTo(Screen.AddTransaction.route)
                    })
            },
            checkScreenContent = {
                CheckScreen(
                    viewModelFactory, paddingValue, onCheckClicked = {}, {})
            },
            checkEditingContent = {
                CheckEditingScreen(
                    viewModelFactory, navState, hapticEffectType
                )
            },
            categoryScreenContent = {
                CategoryScreen(
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValue,
                    onCategoryClicked = {},
                    hapticEffectType = hapticEffectType
                )
            },
            settingsScreenContent = {
                SettingScreen(
                    hapticEffectType,
                    paddingValues = paddingValue,
                    onSettingClicked = { id ->
                        when (id) {
                            0 -> navState.navigateTo(
                                Screen.ColorSelection.route,
                                usePopUpTo = false
                            )
                            1 -> { /* Навигация к звукам */}
                            2 -> navState.navigateTo(Screen.Haptics.route, usePopUpTo = false)
                            3 -> navState.navigateTo(Screen.PinSetup.route, usePopUpTo = false)
                            4 -> navState.navigateTo(Screen.Sync.route, usePopUpTo = false)
                            5 -> navState.navigateTo(Screen.Language.route, usePopUpTo = false)
                            6 -> navState.navigateTo(Screen.Info.route, usePopUpTo = false)
                        }
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleDarkMode = { themeViewModel.toggleTheme(it) }
                )
            },
            historyScreenContent = { isIncome ->
                HistoryScreen(
                    hapticEffectType,
                    viewModelFactory, isIncome, onHistoryItemClicked = {}, paddingValue
                )
            },
            addTransactionContent = { transactionId ->
                AddTransactionScreen(
                    viewModelFactory, navState, transactionId, hapticEffectType
                )
            },
            analyticsScreenContent = { isIncome ->
                AnalyticsScreen(
                    hapticEffectType,
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValue,
                    isIncome = isIncome
                )
            },
            colorSelectionScreenContent = {
                val selectedColor by themeViewModel.appColor.collectAsState()
                ColorSelectionScreen(
                    hapticEffectType,
                    padding = paddingValue,
                    selectedColor = selectedColor,
                    onColorSelected = { newColor ->
                        themeViewModel.setMainColor(newColor)
                        navState.navHostController.popBackStack()
                    }
                )
            },
            hapticScreenContent = {
                HapticScreen(
                    paddingValues = paddingValue,
                    viewModel = viewModel(factory = viewModelFactory)
                )
            },
            passwordScreenContent = {
                PinSetupScreen(
                    onPinSet = {
                        navState.navHostController.popBackStack()
                    },
                    pinCodeManager = pinCodeManager,
                    paddingValues = paddingValue
                )
            },
            infoScreenContent = {
                InfoScreen(paddingValues = paddingValue)
            },
            languageScreenContent = {
                LanguageScreen(
                    paddingValues = paddingValue,
                    currentLanguage = currentLanguage,
                    localePreference = localePreference
                )
            },
            syncScreenContent = {
                SyncScreen(
                    paddingValues = paddingValue,
                    currentIntervalHours = SyncPreference(context).getIntervalHours(),
                    onIntervalChange = {
                        SyncPreference(context).setIntervalHours(it)
                        restartWorker(it, context)
                    }
                )
            }
        )
    }
}

private fun extractBaseRoute(route: String?): String? {
    return route?.substringBefore("?")
}

private fun restartWorker(interval: Int, context: Context) {
    val workRequest = SyncWorker.makeRequest(interval)

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        SyncWorker.WORK_NAME,
        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
        workRequest
    )
}