package com.smorzhok.financeapp.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase

/*предоставляет HistoryScreenViewModel, внедряя в нее GetTransactionsUseCase и GetAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class HistoryScreenViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryScreenViewModel::class.java)) {
            val getTransactionUseCase = GetTransactionsUseCase(transactionRepository)
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            return HistoryScreenViewModel(
                getTransactionUseCase,
                getAccountUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}