package com.smorzhok.financeapp.ui.screen.history

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

/*
управление состоянием UI, связанным с загрузкой списка
транзакций по дате с учетом требования - расходы или доходы
*/
class HistoryScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _historyList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val historyList: StateFlow<UiState<List<Transaction>>> get() = _historyList
    val currency = mutableStateOf("")

    fun loadHistory(from: String, to: String, isIncome: Boolean, context: Context) {
        viewModelScope.launch {
            _historyList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _historyList.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _historyList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = getTransactionsUseCase(id, from, to)
                val history = transactions
                    .filter { it.isIncome == isIncome }
                    .sortedBy { it.time }
                currency.value = transactions.firstOrNull()?.currency ?: "RUB"

                _historyList.value = UiState.Success(history)
            } catch (e: IOException) {
                e.printStackTrace()
                _historyList.value = UiState.Error(e.message)
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    _historyList.value = UiState.Error("Не авторизован")
                } else {
                    _historyList.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}
