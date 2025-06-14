package com.smorzhok.financeapp.domain.model

data class HistoryDto(
    val id: Int,
    val leadingIcon:String,
    val leadingName:String,
    val leadingComment: String?,
    val trailingPrice: Double,
    val trailingTime: String
)