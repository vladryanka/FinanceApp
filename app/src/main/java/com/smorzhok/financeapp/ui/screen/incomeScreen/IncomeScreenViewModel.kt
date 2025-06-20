package com.smorzhok.financeapp.ui.screen.incomeScreen

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

class IncomeScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _incomeList = MutableLiveData<UiState<List<Transaction>>>()
    val incomeList: LiveData<UiState<List<Transaction>>> get() = _incomeList

    fun loadIncomes(from: String, to: String, context: Context) {
        viewModelScope.launch {
            _incomeList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _incomeList.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val accounts = retryWithBackoff { getAccountUseCase() }
                if (accounts.isEmpty()) {
                    _incomeList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = retryWithBackoff { getTransactionsUseCase(id, from, to) }
                val incomes = transactions.filter { it.isIncome }

                _incomeList.value = UiState.Success(incomes)
            } catch (e: Exception) {
                _incomeList.value = UiState.Error(e.message)
            }
        }
    }
}