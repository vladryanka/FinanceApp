package com.smorzhok.financeapp.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTransactionRequest(
    val id: String,
    @SerialName("account_id") val accountId: String,
    @SerialName("category_id") val categoryId: String,
    val value: String,
    val timestamp: Long,
    val comment: String? = null,
    val hidden: Boolean
)
