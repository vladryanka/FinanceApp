package com.smorzhok.financeapp.domain.model

data class ExpenseDto (
    val id: Int,
    val iconLeading: String,
    val textLeading: String,
    val commentLeading: String?,
    val priceTrailing: Double
)