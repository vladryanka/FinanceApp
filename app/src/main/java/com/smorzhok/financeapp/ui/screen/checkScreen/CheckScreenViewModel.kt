package com.smorzhok.financeapp.ui.screen.checkScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import kotlinx.coroutines.launch

class CheckScreenViewModel(
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {
    private val _checkState = MutableLiveData<UiState<Account>>()
    val checkState: LiveData<UiState<Account>> get() = _checkState

    init {
        loadAccount()
    }

    private fun loadAccount() {
        viewModelScope.launch {
            _checkState.value = UiState.Loading
            try {
                val accounts = getAccountUseCase()
                val firstAccount = accounts.firstOrNull()
                if (firstAccount != null) {
                    _checkState.value = UiState.Success(firstAccount)
                } else {
                    _checkState.value = UiState.Error("no_accounts")
                }
            } catch (e: Exception) {
                _checkState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}