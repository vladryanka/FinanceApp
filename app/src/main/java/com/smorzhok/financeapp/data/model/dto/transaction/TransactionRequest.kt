package com.smorzhok.financeapp.data.model.dto.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?
)
