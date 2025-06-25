package com.smorzhok.financeapp.domain.model

data class Transaction(
    val id: Int,
    val accountId: String,
    val categoryName: String,
    val categoryEmoji: String,
    val isIncome: Boolean,
    val amount: Double,
    val time: String,
    val comment: String?
)
