package com.smorzhok.financeapp.data.model.transaction

import com.smorzhok.financeapp.data.model.account.AccountBrief
import com.smorzhok.financeapp.data.model.category.CategoryDto
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val id: Int,
    val account: AccountBrief,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
