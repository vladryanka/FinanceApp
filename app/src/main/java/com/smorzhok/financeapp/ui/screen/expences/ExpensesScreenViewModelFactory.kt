package com.smorzhok.financeapp.ui.screen.expences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase

/*предоставляет ExpensesScreenViewModel, внедряя в нее GetTransactionsUseCase и GetAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class ExpensesScreenViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesScreenViewModel::class.java)) {
            val getTransactionUseCase = GetTransactionsUseCase(transactionRepository)
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            return ExpensesScreenViewModel(
                getTransactionUseCase,
                getAccountUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}