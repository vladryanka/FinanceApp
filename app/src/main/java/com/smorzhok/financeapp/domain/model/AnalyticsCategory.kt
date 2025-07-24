package com.smorzhok.financeapp.domain.model

import com.smorzhok.graphics.AnalyticsCategoryModel

data class AnalyticsCategory(
    val categoryName: String,
    val categoryIcon: String,
    val totalAmount: Double,
    val percent: Int
)

fun AnalyticsCategory.toModel() = AnalyticsCategoryModel(
    categoryName = this.categoryName,
    categoryIcon = this.categoryIcon,
    totalAmount = this.totalAmount,
    percent = this.percent
)
