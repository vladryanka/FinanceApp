package com.smorzhok.financeapp.domain

data class Expenses (
    val id: Int,
    val iconLeadingResId: Int,
    val textLeadingResId: Int,
    val iconTrailingResId: Int,
    val priceTrailing: Int
)