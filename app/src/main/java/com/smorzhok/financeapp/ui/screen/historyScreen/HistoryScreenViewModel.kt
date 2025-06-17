package com.smorzhok.financeapp.ui.screen.historyScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import kotlinx.coroutines.launch

class HistoryScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _historyList = MutableLiveData<UiState<List<Transaction>>>()
    val historyList: LiveData<UiState<List<Transaction>>> get() = _historyList

    fun loadHistory(from: String, to: String) {
        viewModelScope.launch {
            _historyList.value = UiState.Loading
            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _historyList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = getTransactionsUseCase(id, from, to)
                val incomes = transactions.filter { it.isIncome }

                _historyList.value = UiState.Success(incomes)
            } catch (e: Exception) {
                _historyList.value = UiState.Error(e.message)
            }
        }
    }
}