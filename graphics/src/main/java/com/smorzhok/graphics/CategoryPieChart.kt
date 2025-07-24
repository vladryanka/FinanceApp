package com.smorzhok.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryPieChart(
    modifier: Modifier = Modifier,
    data: List<AnalyticsCategoryModel>,
    total: Double
) {
    val colors = listOf(
        Color(0xFF4CAF50), Color(0xFFF44336), Color(0xFF2196F3),
        Color(0xFFFFC107), Color(0xFF9C27B0), Color(0xFFFF9800),
        Color(0xFF009688), Color(0xFFCDDC39), Color(0xFF3F51B5)
    )

    val angleProgress = remember(data) {
        data.mapIndexed { index, item ->
            val startAngle = data.take(index).sumOf {
                it.totalAmount / total * 360
            }
            val sweepAngle = (item.totalAmount / total * 360).toFloat()
            PieChartSlice(
                startAngle = startAngle.toFloat(),
                sweepAngle = sweepAngle,
                color = colors[index % colors.size],
                label = "${item.percent}%",
                name = item.categoryName
            )
        }
    }

    Box(
        modifier = modifier
            .size(240.dp)
            .padding(16.dp)
            .clip(CircleShape)
            .background(Color(0xFFECE6F0)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            angleProgress.forEach { slice ->
                drawArc(
                    color = slice.color,
                    startAngle = slice.startAngle,
                    sweepAngle = slice.sweepAngle,
                    useCenter = true
                )
            }
        }
    }
}
