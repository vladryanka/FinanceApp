package com.smorzhok.financeapp.ui.screen.incomes

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

/*управление состоянием UI, связанным с загрузкой списка транзакций и фильтрация на доходы*/
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _incomeList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val incomeList: StateFlow<UiState<List<Transaction>>> get() = _incomeList
    val currency = mutableStateOf("")

    fun loadIncomes(from: String, to: String, context: Context) {
        viewModelScope.launch {
            _incomeList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _incomeList.value = UiState.Error("no_internet")
                return@launch
            }
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
                currency.value = transactions.firstOrNull()?.currency ?: "RUB"
            } catch (e: IOException) {
                e.printStackTrace()
                _incomeList.value = UiState.Error(e.message)
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    _incomeList.value = UiState.Error("Не авторизован")
                } else {
                    _incomeList.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}

