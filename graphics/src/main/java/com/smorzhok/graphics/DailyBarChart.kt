package com.smorzhok.graphics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyBarChart(
    modifier: Modifier = Modifier,
    stats: List<DailyStats>
) {
    if (stats.isEmpty()) return
    val maxDelta = stats.maxOfOrNull { kotlin.math.abs(it.income - it.expense) } ?: 1.0

    val barWidth = 16.dp
    val spaceBetweenBars = 8.dp
    val maxBarHeight = 100.dp

    LazyRow(
        modifier = modifier
            .padding(16.dp)
            .height(maxBarHeight + 32.dp),
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenBars),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(stats) { stat ->
            val delta = stat.income - stat.expense
            val isIncomePositive = delta >= 0
            val heightRatio = (kotlin.math.abs(delta) / maxDelta).toFloat().coerceIn(0f, 1f)
            val barHeight = maxBarHeight * heightRatio
            val barColor = if (isIncomePositive) Color(0xFF4CAF50) else Color(0xFFF44336)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.width(barWidth)
            ) {
                Box(
                    modifier = Modifier
                        .height(maxBarHeight)
                        .width(barWidth),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (delta != 0.0) {
                        Box(
                            modifier = Modifier
                                .height(barHeight)
                                .width(barWidth)
                                .background(barColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stat.date.dayOfMonth.toString(),
                    style = TextStyle(
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}