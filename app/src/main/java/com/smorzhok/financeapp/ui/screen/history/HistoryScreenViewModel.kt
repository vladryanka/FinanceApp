package com.smorzhok.financeapp.ui.screen.history

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

/*
управление состоянием UI, связанным с загрузкой списка
транзакций по дате с учетом требования - расходы или доходы
*/
class HistoryScreenViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _historyList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val historyList: StateFlow<UiState<List<Transaction>>> get() = _historyList
    val currency = mutableStateOf("")

    fun loadHistory(from: String, to: String, isIncome: Boolean) {
        viewModelScope.launch {
            _historyList.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _historyList.value = UiState.Error(ErrorList.NoAccount)
                    return@launch
                }

                val accountId = accounts.first().id
                val transactions = getTransactionsUseCase(accountId, from, to)
                val history = transactions
                    .filter { it.isIncome == isIncome }
                    .sortedBy { it.time }

                currency.value = transactions.firstOrNull()?.currency ?: "RUB"
                _historyList.value = UiState.Success(history)

            } catch (e: HttpException) {
                e.printStackTrace()
                _historyList.value = when (e.code()) {
                    401 -> UiState.Error(ErrorList.NotAuthorized)
                    else -> UiState.Error(ErrorList.ServerError)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _historyList.value = UiState.Error(ErrorList.ServerError)
            }
        }
    }
}
