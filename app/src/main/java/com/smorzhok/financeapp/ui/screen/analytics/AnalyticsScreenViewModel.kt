package com.smorzhok.financeapp.ui.screen.analytics

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
import javax.inject.Inject

class AnalyticsScreenViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    ) : ViewModel() {

    private val _transactionList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val transactionList: StateFlow<UiState<List<Transaction>>> get() = _transactionList
    val currency = mutableStateOf("")

    fun loadTransactions(from: String, to: String, isIncome: Boolean, context: Context) {
        viewModelScope.launch {
            _transactionList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _transactionList.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _transactionList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = getTransactionsUseCase(id, from, to)
                val transactionsFiltered = transactions
                    .filter { it.isIncome == isIncome }
                    .sortedBy { it.time }
                currency.value = transactions.firstOrNull()?.currency ?: "RUB"

                _transactionList.value = UiState.Success(transactionsFiltered)
            } catch (e: IOException) {
                e.printStackTrace()
                _transactionList.value = UiState.Error(e.message)
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    _transactionList.value = UiState.Error("Не авторизован")
                } else {
                    _transactionList.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}