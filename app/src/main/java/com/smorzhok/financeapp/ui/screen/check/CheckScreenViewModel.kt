package com.smorzhok.financeapp.ui.screen.check

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.account.UpdateAccountsUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.UpdateTransactionUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.commonitems.retryWithBackoff
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.formatter.formatCurrencySymbolToCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*управление состоянием UI, связанным с загрузкой списка аккаунтов*/
class CheckScreenViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountsUseCase: UpdateAccountsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {
    private val _checkState = MutableLiveData<UiState<Account>>()
    val checkState: LiveData<UiState<Account>> get() = _checkState

    private val _dialogueMessage = MutableLiveData<String?>()
    val dialogueMessage: LiveData<String?> get() = _dialogueMessage

    val name = mutableStateOf("")
    val balance = mutableStateOf("")
    val currency = mutableStateOf("")

    private lateinit var account: Account

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAccount(context: Context) {
        viewModelScope.launch {
            _checkState.postValue(UiState.Loading)
            val account = Account(
                account.id,
                name.value,
                balance.value.toDouble(),
                formatCurrencySymbolToCode(currency.value)
            )
            if (!isNetworkAvailable(context)) {
                val error = context.getString(R.string.network_error)
                _dialogueMessage.value = context.getString(R.string.error)
                _checkState.postValue(UiState.Error(error))
                return@launch
            }
            try {
                withContext(Dispatchers.IO) {
                    updateAccountsUseCase(
                        account
                    )
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val fromDate = LocalDate.of(2001, 1, 1)//нам неизвестна дата начала транзакций
                    val toDate = LocalDate.now()
                    val transactions = getTransactionsUseCase(
                        account.id,
                        fromDate.format(dateFormatter),
                        toDate.format(dateFormatter)
                    )
                    Log.d("Doing", "All tr = $transactions")
                    transactions.forEach{
                        val newCurrency = formatCurrencyCodeToSymbol(account.currency)
                        val newTransaction = it.copy(currency = newCurrency)
                        updateTransactionUseCase(newTransaction)
                    }
                }
                _checkState.postValue(UiState.Success(account))
                _dialogueMessage.value = context.getString(R.string.account_updated_successfully)
            } catch (e: Exception) {
                val error = when (e) {
                    is IOException -> e.message ?: context.getString(R.string.network_error)
                    is HttpException -> e.message ?: context.getString(R.string.server_error)
                    else -> context.getString(R.string.unknown_error)
                }
                _dialogueMessage.postValue(error)
                _checkState.postValue(UiState.Error(error))
            }
        }
    }

    fun dialogueShown() {
        _dialogueMessage.value = null
    }

    fun loadAccount(context: Context) {
        viewModelScope.launch {
            _checkState.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _checkState.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val accounts =
                    withContext(Dispatchers.IO) { retryWithBackoff { getAccountUseCase() } }
                val firstAccount = accounts.firstOrNull()
                if (firstAccount != null) {
                    _checkState.value = UiState.Success(firstAccount)
                    name.value = firstAccount.name
                    balance.value = firstAccount.balance.toString()
                    currency.value = formatCurrencyCodeToSymbol(firstAccount.currency)
                    account = firstAccount
                } else {
                    _checkState.value = UiState.Error("no_accounts")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _checkState.value = UiState.Error(e.message ?: R.string.network_error.toString())
            } catch (e: HttpException) {
                e.printStackTrace()
                _checkState.value = UiState.Error(e.message ?: (R.string.server_error).toString())
            }
        }
    }
}
