package com.smorzhok.financeapp.data.model.dto.account

import kotlinx.serialization.Serializable
/*сущность с бэка AccountDto*/
@Serializable
data class AccountDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)
