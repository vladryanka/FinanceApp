package com.smorzhok.network.dto.account

import kotlinx.serialization.Serializable

/*сущность с бэка AccountBrief*/
@Serializable
class AccountBrief (
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
