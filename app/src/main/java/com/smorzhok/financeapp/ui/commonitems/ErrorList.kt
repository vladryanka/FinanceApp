package com.smorzhok.financeapp.ui.commonitems

sealed class ErrorList{
    data object NoInternet : ErrorList()
    data object NotAuthorized : ErrorList()
    data object ServerError : ErrorList()
    data object NoAccount : ErrorList()
}
