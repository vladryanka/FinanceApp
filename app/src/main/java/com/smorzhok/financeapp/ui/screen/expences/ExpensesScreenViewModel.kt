package com.smorzhok.financeapp.ui.screen.expences

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/*управление состоянием UI, связанным с загрузкой списка транзакций и фильтрация на расходы*/
class ExpensesScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {
    private val _expenseList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val expenseList: StateFlow<UiState<List<Transaction>>> get() = _expenseList
    val currency = mutableStateOf("")

    fun loadTransactions(from: String, to: String, context: Context) {
        viewModelScope.launch {
            _expenseList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _expenseList.value = UiState.Error("no_internet")
                return@launch
            }

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _expenseList.value = UiState.Error("no_account")
                    return@launch
                }
                val id = accounts.first().id
                val transactions = getTransactionsUseCase(id, from, to)
                val expenses = transactions.filter { !it.isIncome }

                _expenseList.value = UiState.Success(expenses)
                currency.value = transactions.firstOrNull()?.currency ?: "RUB"

            } catch (e: IOException) {
                e.printStackTrace()
                _expenseList.value = UiState.Error(e.message)
            }
            catch (e: HttpException) {
                e.printStackTrace()
                if(e.code() == 401){
                    _expenseList.value = UiState.Error("Не авторизован")
                } else{
                    _expenseList.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}
