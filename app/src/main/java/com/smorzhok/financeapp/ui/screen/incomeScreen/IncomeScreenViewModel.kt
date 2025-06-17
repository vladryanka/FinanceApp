package com.smorzhok.financeapp.ui.screen.incomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import kotlinx.coroutines.launch

class IncomeScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _incomeList = MutableLiveData<UiState<List<Transaction>>>()
    val incomeList: LiveData<UiState<List<Transaction>>> get() = _incomeList

    fun loadIncomes(from: String, to: String) {
        viewModelScope.launch {
            _incomeList.value = UiState.Loading
            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _incomeList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = getTransactionsUseCase(id, from, to)
                val incomes = transactions.filter { it.isIncome }

                _incomeList.value = UiState.Success(incomes)
            } catch (e: Exception) {
                _incomeList.value = UiState.Error(e.message)
            }
        }
    }
}