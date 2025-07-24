package com.smorzhok.financeapp.ui.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt


@Composable
fun SyncScreen(
    paddingValues: PaddingValues,
    currentIntervalHours: Int,
    onIntervalChange: (Int) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(currentIntervalHours.toFloat()) }

    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = paddingValues.calculateTopPadding() + 8.dp
        )
    ) {
        Text("Синхронизация каждые ${sliderPosition.roundToInt()} ч.")

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            },
            onValueChangeFinished = {
                onIntervalChange(sliderPosition.roundToInt())
            },
            valueRange = 1f..12f,
            steps = 10
        )
    }
}