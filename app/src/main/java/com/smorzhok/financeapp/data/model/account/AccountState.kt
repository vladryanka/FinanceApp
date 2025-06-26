package com.smorzhok.financeapp.data.model.account

import kotlinx.serialization.Serializable

/*сущность с бэка AccountState*/
@Serializable
data class AccountState (
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
