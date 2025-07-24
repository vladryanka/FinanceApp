package com.smorzhok.financeapp.ui.screen.check

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.account.UpdateAccountsUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.commonitems.ErrorList
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.formatter.formatCurrencySymbolToCode
import com.smorzhok.graphics.DailyStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/*управление состоянием UI, связанным с загрузкой списка аккаунтов*/
class CheckScreenViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountsUseCase: UpdateAccountsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {
    private val _checkState = MutableStateFlow<UiState<Account>>(UiState.Loading)
    val checkState: StateFlow<UiState<Account>> get() = _checkState

    private val _dialogueMessage = MutableStateFlow<String?>(null)
    val dialogueMessage: StateFlow<String?> get() = _dialogueMessage

    private val _dailyStats = MutableStateFlow<List<DailyStats>>(emptyList())
    val dailyStats: StateFlow<List<DailyStats>> = _dailyStats

    val name = mutableStateOf("")
    val balance = mutableStateOf("")
    val currency = mutableStateOf("")

    private lateinit var account: Account

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getDailyStatsForCurrentMonth(): List<DailyStats> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val transactionDateFormatter = DateTimeFormatter.ISO_DATE_TIME

        val from = startOfMonth.format(dateFormatter)
        val to = today.format(dateFormatter)

        val transactions = getTransactionsUseCase(52, from, to)

        val transactionsByDay = transactions.groupBy { transaction ->
            LocalDate.parse(transaction.time, transactionDateFormatter)
        }

        return (0 until today.dayOfMonth).map { dayOffset ->
            val date = startOfMonth.plusDays(dayOffset.toLong())

            val dayTransactions = transactionsByDay[date] ?: emptyList()

            val income = dayTransactions.filter { it.isIncome }.sumOf { it.amount }
            val expense = dayTransactions.filter { !it.isIncome }.sumOf { it.amount }

            DailyStats(date, income, expense)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDailyStats() {
        viewModelScope.launch {
            try {
                val stats = getDailyStatsForCurrentMonth()
                _dailyStats.value = stats
                Log.d("Doing", _dailyStats.value.toString())
            } catch (_: Exception) {
                _dailyStats.value = emptyList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAccount(context: Context) {
        viewModelScope.launch {
            _checkState.value = UiState.Loading
            account = Account(
                id = account.id,
                name = name.value,
                balance = balance.value.toDouble(),
                currency = formatCurrencySymbolToCode(currency.value)
            )

            try {
                updateAccountsUseCase(
                    account
                )

                _checkState.value = UiState.Success(account)
                _dialogueMessage.value = context.getString(R.string.account_updated_successfully)
            } catch (e: Exception) {
                val error = when (e) {
                    is IOException -> ErrorList.NoInternet
                    is HttpException -> ErrorList.NotAuthorized
                    else -> ErrorList.ServerError
                }
                _dialogueMessage.value = error.toString()
                _checkState.value = UiState.Error(error)
            }
        }
    }

    fun dialogueShown() {
        _dialogueMessage.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAccount() {
        viewModelScope.launch {
            _checkState.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                val firstAccount = accounts.firstOrNull()

                if (firstAccount != null) {
                    _checkState.value = UiState.Success(firstAccount)
                    name.value = firstAccount.name
                    balance.value = firstAccount.balance.toString()
                    currency.value = formatCurrencyCodeToSymbol(firstAccount.currency)
                    account = firstAccount
                    loadDailyStats()
                } else {
                    _checkState.value = UiState.Error(ErrorList.NoAccount)
                }

            } catch (e: HttpException) {
                e.printStackTrace()
                _checkState.value = when (e.code()) {
                    401 -> UiState.Error(ErrorList.NotAuthorized)
                    else -> UiState.Error(ErrorList.ServerError)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _checkState.value = UiState.Error(ErrorList.ServerError)
            }
        }
    }
}
