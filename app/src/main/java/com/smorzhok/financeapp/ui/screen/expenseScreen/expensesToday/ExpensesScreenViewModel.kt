package com.smorzhok.financeapp.ui.screen.expenseScreen.expensesToday

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.screen.commonItems.isNetworkAvailable
import com.smorzhok.financeapp.ui.screen.commonItems.retryWithBackoff
import kotlinx.coroutines.launch

class ExpensesScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {
    private val _expenseList = MutableLiveData<UiState<List<Transaction>>>()
    val expenseList: LiveData<UiState<List<Transaction>>> get() = _expenseList

    fun loadTransactions(from: String, to: String, context: Context) {
        viewModelScope.launch {
            _expenseList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _expenseList.value = UiState.Error("no_internet")
                return@launch
            }

            try {
                val accounts = retryWithBackoff {
                    getAccountUseCase()
                }
                if (accounts.isEmpty()) {
                    _expenseList.value = UiState.Error("no_account")
                    return@launch
                }
                val id = accounts.first().id
                val transactions = retryWithBackoff {
                    getTransactionsUseCase(id, from, to)
                }
                val expenses = transactions.filter { !it.isIncome }

                _expenseList.value = UiState.Success(expenses)

            } catch (e: Exception) {
                _expenseList.value = UiState.Error(e.message)
            }
        }
    }
}