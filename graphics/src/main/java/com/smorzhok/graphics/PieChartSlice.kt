package com.smorzhok.graphics

import androidx.compose.ui.graphics.Color

data class PieChartSlice(
    val startAngle: Float,
    val sweepAngle: Float,
    val color: Color,
    val label: String,
    val name: String
)