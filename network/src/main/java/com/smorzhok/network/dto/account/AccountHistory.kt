package com.smorzhok.network.dto.account

import kotlinx.serialization.Serializable

/*сущность с бэка AccountHistory*/
@Serializable
data class AccountHistory(
    val id: Int,
    val accountId: Int,
    val changeType: ChangeType,
    val previousState: AccountState,
    val newState: AccountState,
    val changeTimestamp: String,
    val createdAt: String
)

/*сущность с бэка ChangeType*/
enum class ChangeType{
    CREATION, MODIFICATION
}
