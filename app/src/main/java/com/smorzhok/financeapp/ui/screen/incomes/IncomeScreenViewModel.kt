package com.smorzhok.financeapp.ui.screen.incomes

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetCurrencyUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.commonitems.retryWithBackoff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/*управление состоянием UI, связанным с загрузкой списка транзакций и фильтрация на доходы*/
class IncomeScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
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
                val accounts = withContext(Dispatchers.IO) {
                    retryWithBackoff { getAccountUseCase() }
                }
                if (accounts.isEmpty()) {
                    _incomeList.value = UiState.Error("no_accounts")
                    return@launch
                }

                val id = accounts.first().id

                val transactions = withContext(Dispatchers.IO) {
                    retryWithBackoff { getTransactionsUseCase(id, from, to) }
                }
                val incomes = transactions.filter { it.isIncome }

                _incomeList.value = UiState.Success(incomes)
            } catch (e: IOException) {
                e.printStackTrace()
                _incomeList.value = UiState.Error(e.message)
            }
            catch (e: HttpException) {
                e.printStackTrace()
                _incomeList.value = UiState.Error(e.message ?: R.string.server_error.toString())
            }
            currency.value = getCurrencyUseCase()
        }
    }
}

