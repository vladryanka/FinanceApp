package com.smorzhok.financeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.account.UpdateAccountsUseCase

/*предоставляет CheckScreenViewModel, внедряя в нее GetAccountUseCase и UpdateAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class CheckScreenViewModelFactory(
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckScreenViewModel::class.java)) {
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            val updateAccountUseCase = UpdateAccountsUseCase(accountRepository)
            return CheckScreenViewModel(getAccountUseCase, updateAccountUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
