package com.smorzhok.financeapp.data.model

data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String, // Date?
    val updatedAt: String // Date?
)