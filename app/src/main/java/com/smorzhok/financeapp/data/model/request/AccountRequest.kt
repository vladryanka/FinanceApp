package com.smorzhok.financeapp.data.model.request

import kotlinx.serialization.SerialName

data class AccountRequest(
    val id: String,
    @SerialName("account_name")
    val accountName: String,
    val currency: String
)