package com.smorzhok.financeapp.ui.screen.incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetCurrencyUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase

/*предоставляет IncomeScreenViewModel, внедряя в нее GetTransactionsUseCase и GetAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class IncomeScreenViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeScreenViewModel::class.java)) {
            val getTransactionUseCase = GetTransactionsUseCase(transactionRepository)
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            val getCurrencyUseCase = GetCurrencyUseCase(transactionRepository)
            return IncomeScreenViewModel(
                getTransactionUseCase,
                getAccountUseCase,
                getCurrencyUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}