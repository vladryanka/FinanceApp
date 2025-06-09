package com.smorzhok.financeapp.domain.model

data class Balance(
    val id: Int,
    val iconLeadingResId: Int,
    val textLeadingResId: Int,
    val iconTrailingResId: Int,
    val priceTrailing: Int
): OnCheckScreen

data class Currency(
    val id: Int,
    val textLeadingResId: Int,
    val currencyTrailing: Int,
    val iconTrailingResId: Int,
): OnCheckScreen

interface OnCheckScreen