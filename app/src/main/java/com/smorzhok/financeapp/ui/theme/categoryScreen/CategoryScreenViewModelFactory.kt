package com.smorzhok.financeapp.ui.theme.categoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.usecase.FinanceRepository

@Suppress("UNCHECKED_CAST")
class CategoryScreenViewModelFactory (
    private val financeRepository: FinanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryScreenViewModel::class.java)) {
            return CategoryScreenViewModel(financeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}