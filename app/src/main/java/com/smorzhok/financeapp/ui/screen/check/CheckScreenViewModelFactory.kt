package com.smorzhok.financeapp.ui.screen.check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.AccountRepository
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.account.UpdateAccountsUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.UpdateTransactionUseCase

/*предоставляет CheckScreenViewModel, внедряя в нее GetAccountUseCase и UpdateAccountUseCase*/
@Suppress("UNCHECKED_CAST")
class CheckScreenViewModelFactory(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckScreenViewModel::class.java)) {
            val getAccountUseCase = GetAccountUseCase(accountRepository)
            val updateAccountUseCase = UpdateAccountsUseCase(accountRepository)
            val updateTransactionUseCase = UpdateTransactionUseCase(transactionRepository)
            val getTransactionsUseCase = GetTransactionsUseCase(transactionRepository)
            return CheckScreenViewModel(
                getAccountUseCase,
                updateAccountUseCase,
                updateTransactionUseCase,
                getTransactionsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
