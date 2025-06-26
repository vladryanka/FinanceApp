package com.smorzhok.financeapp.data.model.account

import kotlinx.serialization.Serializable

/*сущность с бэка UpdateAccountsRequest*/
@Serializable
data class UpdateAccountsRequest(
    val accounts: List<AccountBrief>
)
