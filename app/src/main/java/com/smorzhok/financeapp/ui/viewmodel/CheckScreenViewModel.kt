package com.smorzhok.financeapp.ui.viewmodel

import android.content.Context
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
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.commonitems.retryWithBackoff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/*управление состоянием UI, связанным с загрузкой списка аккаунтов*/
class CheckScreenViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountsUseCase: UpdateAccountsUseCase
) : ViewModel() {
    private val _checkState = MutableLiveData<UiState<Account>>()
    val checkState: LiveData<UiState<Account>> get() = _checkState
    val name = mutableStateOf("")
    val balance = mutableStateOf("")
    val currency = mutableStateOf("")

    private lateinit var account: Account

    fun updateAccount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                updateAccountsUseCase(
                    Account(
                        account.id,
                        name.value,
                        balance.value.toDouble(),
                        currency.value
                    )
                )
            }
        }
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
