package com.smorzhok.financeapp.data.model.transaction

import com.smorzhok.financeapp.data.model.category.CategoryDto
import com.smorzhok.financeapp.data.model.account.AccountBrief
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*Сущность с бэка TransactionDto*/
@Serializable
data class TransactionDto(
    val id: Int,
    @SerialName("account")
    val account: AccountBrief,
    @SerialName("category")
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
