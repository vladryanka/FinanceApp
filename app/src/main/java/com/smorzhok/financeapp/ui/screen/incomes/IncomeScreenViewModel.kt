package com.smorzhok.financeapp.ui.screen.incomes

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

/*управление состоянием UI, связанным с загрузкой списка транзакций и фильтрация на доходы*/
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _incomeList = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val incomeList: StateFlow<UiState<List<Transaction>>> get() = _incomeList
    val currency = mutableStateOf("")

    fun loadIncomes(from: String, to: String) {
        viewModelScope.launch {
            _incomeList.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _incomeList.value = UiState.Error(ErrorList.NoAccount)
                    return@launch
                }

                val accountId = accounts.first().id
                val transactions = getTransactionsUseCase(accountId, from, to)
                val incomes = transactions.filter { it.isIncome }
                _incomeList.value = UiState.Success(incomes)
                currency.value = incomes.firstOrNull()?.currency ?: "RUB"

            } catch (e: HttpException) {
                e.printStackTrace()
                _incomeList.value = when (e.code()) {
                    401 -> UiState.Error(ErrorList.NotAuthorized)
                    else -> UiState.Error(ErrorList.ServerError)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _incomeList.value = UiState.Error(ErrorList.ServerError)
            }
        }
    }
}

