package com.smorzhok.financeapp.ui.theme.incomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.usecase.FinanceRepository

@Suppress("UNCHECKED_CAST")
class IncomeScreenViewModelFactory(
    private val financeRepository: FinanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeScreenViewModel::class.java)) {
            return IncomeScreenViewModel(financeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}