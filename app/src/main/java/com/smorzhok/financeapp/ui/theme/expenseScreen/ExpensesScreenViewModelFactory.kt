package com.smorzhok.financeapp.ui.theme.expenseScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.usecase.FinanceRepository

@Suppress("UNCHECKED_CAST")
class ExpensesScreenViewModelFactory(
    private val financeRepository: FinanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesScreenViewModel::class.java)) {
            return ExpensesScreenViewModel(financeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}