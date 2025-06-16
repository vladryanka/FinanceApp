package com.smorzhok.financeapp.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteTransactionRequest(
    val id: String
)