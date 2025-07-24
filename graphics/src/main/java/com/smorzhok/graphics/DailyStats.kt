package com.smorzhok.graphics

import java.time.LocalDate

data class DailyStats(
    val date: LocalDate,
    val income: Double,
    val expense: Double
)