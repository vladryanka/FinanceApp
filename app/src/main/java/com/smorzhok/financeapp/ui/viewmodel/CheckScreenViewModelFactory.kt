package com.smorzhok.financeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase

/*предоставляет CheckScreenViewModel, внедряя в нее GetAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class CheckScreenViewModelFactory(
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckScreenViewModel::class.java)) {
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            return CheckScreenViewModel(getAccountUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
