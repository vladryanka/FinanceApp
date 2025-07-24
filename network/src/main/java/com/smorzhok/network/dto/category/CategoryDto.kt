package com.smorzhok.network.dto.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*сущность с бэка CategoryDto*/
@Serializable
data class CategoryDto (
    val id: Int,
    val name: String,
    @SerialName("emoji")
    val emoji: String,
    @SerialName("isIncome")
    val isIncome: Boolean
)
