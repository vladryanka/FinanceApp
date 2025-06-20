package com.smorzhok.financeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Int,
    val account: AccountBrief,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)