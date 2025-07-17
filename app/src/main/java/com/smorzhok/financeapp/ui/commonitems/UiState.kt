package com.smorzhok.financeapp.ui.commonitems

/*Представляет обобщённое состояние UI для отображения данных*/
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: ErrorList) : UiState<Nothing>()
}
