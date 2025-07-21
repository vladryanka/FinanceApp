package com.smorzhok.financeapp.ui.screen.expences

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.commonitems.ErrorList
import com.smorzhok.financeapp.ui.commonitems.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/*управление состоянием UI, связанным с загрузкой списка транзакций и фильтрация на расходы*/
class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _expenseList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val expenseList: StateFlow<UiState<List<Transaction>>> get() = _expenseList

    val currency = mutableStateOf("")

    fun loadTransactions(from: String, to: String) {
        viewModelScope.launch {
            _expenseList.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _expenseList.value = UiState.Error(ErrorList.NoAccount)
                    return@launch
                }

                val accountId = accounts.first().id
                val transactions = getTransactionsUseCase(accountId, from, to)
                val expenses = transactions.filter { !it.isIncome }
                _expenseList.value = UiState.Success(expenses)
                currency.value = expenses.firstOrNull()?.currency ?: "RUB"

            } catch (e: HttpException) {
                e.printStackTrace()
                _expenseList.value = when (e.code()) {
                    401 -> UiState.Error(ErrorList.NotAuthorized)
                    else -> UiState.Error(ErrorList.ServerError)
                }

                Log.d("Doing", e.message())

            } catch (e: Exception) {
                e.printStackTrace()
                _expenseList.value = UiState.Error(ErrorList.ServerError)
            }
        }
    }
}