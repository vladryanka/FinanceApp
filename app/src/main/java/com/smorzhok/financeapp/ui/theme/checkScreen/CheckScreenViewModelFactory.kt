package com.smorzhok.financeapp.ui.theme.checkScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.usecase.FinanceRepository

@Suppress("UNCHECKED_CAST")
class CheckScreenViewModelFactory(
    private val financeRepository: FinanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckScreenViewModel::class.java)) {
            return CheckScreenViewModel(financeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}