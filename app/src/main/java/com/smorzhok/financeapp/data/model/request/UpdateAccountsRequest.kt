package com.smorzhok.financeapp.data.model.request

import com.smorzhok.financeapp.data.model.AccountBrief
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountsRequest(
    val accounts: List<AccountBrief>
)