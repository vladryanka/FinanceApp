package com.smorzhok.financeapp.domain.model

data class Account(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)