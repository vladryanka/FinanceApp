package com.smorzhok.financeapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto (
    val id: Int,
    val name: String,
    @SerialName("emoji")
    val emoji: String,
    @SerialName("isIncome")
    val isIncome: Boolean
)