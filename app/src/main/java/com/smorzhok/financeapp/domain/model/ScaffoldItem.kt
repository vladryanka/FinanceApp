package com.smorzhok.financeapp.domain.model

import androidx.compose.ui.graphics.Color
import com.smorzhok.financeapp.R

data class ScaffoldItem(
    val leadingImageResId: Int? = R.drawable.refresh,
    val trailingImageResId: Int? = R.drawable.refresh,
    val textResId: Int = R.string.expenses_today,
    val backgroundColor: Color
)
