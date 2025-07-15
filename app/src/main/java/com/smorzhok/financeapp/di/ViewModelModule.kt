package com.smorzhok.financeapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.ui.ViewModelFactory
import com.smorzhok.financeapp.ui.screen.add_transaction.AddTransactionViewModel
import com.smorzhok.financeapp.ui.screen.analytics.AnalyticsScreenViewModel
import com.smorzhok.financeapp.ui.screen.category.CategoryScreenViewModel
import com.smorzhok.financeapp.ui.screen.check.CheckScreenViewModel
import com.smorzhok.financeapp.ui.screen.expences.ExpensesScreenViewModel
import com.smorzhok.financeapp.ui.screen.history.HistoryScreenViewModel
import com.smorzhok.financeapp.ui.screen.incomes.IncomeScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddTransactionViewModel::class)
    fun bindsAddTransactionViewModel(viewModel: AddTransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryScreenViewModel::class)
    fun bindsCategoryScreenViewModel(viewModel: CategoryScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckScreenViewModel::class)
    fun bindsCheckScreenViewModel(viewModel: CheckScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnalyticsScreenViewModel::class)
    fun bindsAnalyticsScreenViewModel(viewModel: AnalyticsScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesScreenViewModel::class)
    fun bindsExpensesScreenViewModel(viewModel: ExpensesScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryScreenViewModel::class)
    fun bindsHistoryScreenViewModel(viewModel: HistoryScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeScreenViewModel::class)
    fun bindsIncomeScreenViewModel(viewModel: IncomeScreenViewModel): ViewModel

}