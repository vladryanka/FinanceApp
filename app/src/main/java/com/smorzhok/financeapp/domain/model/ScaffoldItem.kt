package com.smorzhok.financeapp.domain.model

import com.smorzhok.financeapp.R

data class ScaffoldItem(
    val imageResId: Int? = R.drawable.refresh,
    val textResId: Int = R.string.expenses_today,
)