package com.smorzhok.financeapp.data.model

data class Transaction(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: String,
    val transactionDate: String,//Date?
    val comment: String?,
    val createdAt: String,//Date?
    val updatedAt: String//Date?
)