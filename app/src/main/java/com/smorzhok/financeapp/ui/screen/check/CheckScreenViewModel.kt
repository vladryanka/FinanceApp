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
import com.smorzhok.financeapp.ui.commonitems.ErrorList
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.formatter.formatCurrencySymbolToCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/*управление состоянием UI, связанным с загрузкой списка аккаунтов*/
class CheckScreenViewModel @Inject constructor(
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
