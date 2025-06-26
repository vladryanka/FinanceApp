package com.smorzhok.financeapp.data.model.account

import kotlinx.serialization.Serializable

/*сущность с бэка AccountHistoryResponse*/
@Serializable
data class AccountHistoryResponse (
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: AccountHistory
)
