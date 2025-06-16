package com.smorzhok.financeapp.data.model.response

import com.smorzhok.financeapp.data.model.AccountBrief

data class AccountsResponse(
    val accounts: List<AccountBrief>
)