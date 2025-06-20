package com.smorzhok.financeapp.data.model.response

import com.smorzhok.financeapp.data.model.AccountHistory
import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponse (
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: AccountHistory
)