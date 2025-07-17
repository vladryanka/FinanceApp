package com.smorzhok.financeapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: Int,
    val iconLeading: String,
    val textLeading: String,
    val isIncome: Boolean
)
