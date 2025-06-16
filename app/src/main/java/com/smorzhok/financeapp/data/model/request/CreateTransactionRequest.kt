package com.smorzhok.financeapp.data.model.request

import kotlinx.serialization.SerialName

data class CreateTransactionRequest(
    @SerialName("account_id")
    val accountId: String,
    @SerialName("category_id")
    val categoryId: String?,
    val value: String,
    val timestamp: Long,
    val comment: String? = null,
    val hidden: Boolean
)