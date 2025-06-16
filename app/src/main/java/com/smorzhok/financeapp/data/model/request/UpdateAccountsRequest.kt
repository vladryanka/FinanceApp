package com.smorzhok.financeapp.data.model.request

import com.smorzhok.financeapp.data.model.AccountBrief

data class UpdateAccountsRequest(
    val accounts: List<AccountBrief>
)