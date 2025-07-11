package com.smorzhok.financeapp.domain.model

data class TransactionEdit(
    val accountId: Int,
    val currency: String,
    val name: String,
    val amount: String,
    val category: Category,
    val dateTime: String,
    val comment: String?
)