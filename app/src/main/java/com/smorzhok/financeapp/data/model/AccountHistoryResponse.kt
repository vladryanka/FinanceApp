package com.smorzhok.financeapp.data.model

data class AccountHistoryResponse (
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: AccountHistory
)

