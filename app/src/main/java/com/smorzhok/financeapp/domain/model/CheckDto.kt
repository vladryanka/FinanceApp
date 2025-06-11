package com.smorzhok.financeapp.domain.model

data class CheckDto(
    val id: Int,
    val leadingIcon: String,
    val balance: Double,
    val currency: String
)