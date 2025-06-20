package com.smorzhok.financeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
class AccountBrief (
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)