package com.smorzhok.network.dto.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
