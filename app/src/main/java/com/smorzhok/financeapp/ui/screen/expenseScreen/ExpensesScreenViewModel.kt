package com.smorzhok.financeapp.ui.screen.expenseScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import kotlinx.coroutines.launch

class ExpensesScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {
    private val _expenseList = MutableLiveData<UiState<List<Transaction>>>()
    val expenseList: LiveData<UiState<List<Transaction>>> get() = _expenseList

    fun loadTransactions(from: String, to: String) {
        viewModelScope.launch {
            _expenseList.value = UiState.Loading
            try {
                val accounts = getAccountUseCase()
                Log.d("Doing", "Accounts = "+accounts.toString())
                if (accounts.isEmpty()) {
                    _expenseList.value = UiState.Error("no_account")
                    return@launch
                }
                val id = accounts.first().id
                Log.d("Doing", "id = "+id.toString())
                val transactions = getTransactionsUseCase(id, from, to)
                val expenses = transactions.filter { !it.isIncome }

                Log.d("Doing", "transactions = " + transactions.toString())
                _expenseList.value = UiState.Success(expenses)

            } catch (e: Exception) {
                _expenseList.value = UiState.Error(e.message)
            }
        }
    }

}