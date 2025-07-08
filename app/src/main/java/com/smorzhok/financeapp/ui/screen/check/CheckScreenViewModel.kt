package com.smorzhok.financeapp.ui.screen.check

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.account.UpdateAccountsUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.formatter.formatCurrencySymbolToCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/*управление состоянием UI, связанным с загрузкой списка аккаунтов*/
class CheckScreenViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountsUseCase: UpdateAccountsUseCase
) : ViewModel() {
    private val _checkState = MutableStateFlow<UiState<Account>>(UiState.Loading)
    val checkState: StateFlow<UiState<Account>> get() = _checkState

    private val _dialogueMessage = MutableStateFlow<String?>(null)
    val dialogueMessage: StateFlow<String?> get() = _dialogueMessage

    val name = mutableStateOf("")
    val balance = mutableStateOf("")
    val currency = mutableStateOf("")

    private lateinit var account: Account

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

            if (!isNetworkAvailable(context)) {
                val error = context.getString(R.string.network_error)
                _dialogueMessage.value = context.getString(R.string.error)
                _checkState.value = UiState.Error(error)
                return@launch
            }
            try {
                updateAccountsUseCase(
                    account
                )

                _checkState.value = UiState.Success(account)
                _dialogueMessage.value = context.getString(R.string.account_updated_successfully)
            } catch (e: Exception) {
                val error = when (e) {
                    is IOException -> e.message ?: context.getString(R.string.network_error)
                    is HttpException -> e.message ?: context.getString(R.string.server_error)
                    else -> context.getString(R.string.unknown_error)
                }
                _dialogueMessage.value = error
                _checkState.value = UiState.Error(error)
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
                val accounts = getAccountUseCase()
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
                if (e.code() == 401) {
                    _checkState.value = UiState.Error("Не авторизован")
                } else {
                    _checkState.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}
