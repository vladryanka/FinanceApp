package com.smorzhok.financeapp.ui.screen.setting

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.ui.commonitems.HapticViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.smorzhok.financeapp.R

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HapticScreen(
    paddingValues: PaddingValues,
    viewModel: HapticViewModel
) {
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()
    val currentEffect by viewModel.hapticEffect.collectAsState()

    val effects = listOf(stringResource(R.string.click), stringResource(R.string.text_handle_move))
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(
            vertical = paddingValues.calculateTopPadding()+8.dp,
            horizontal = 16.dp
        )
    ) {
        Text(stringResource(R.string.haptics), style = MaterialTheme.typography.titleLarge)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(stringResource(R.string.enable_haptics))
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = hapticEnabled, onCheckedChange = {
                viewModel.toggleHaptic(it)
            })
        }

        if (hapticEnabled) {
            Text(stringResource(R.string.choose_effect))
            Spacer(Modifier.height(8.dp))
            effects.forEach { effect ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewModel.setEffect(effect)
                            performHapticFeedback(context, effect)
                        }
                ) {
                    RadioButton(selected = currentEffect == effect, onClick = {
                        viewModel.setEffect(effect)
                        performHapticFeedback(context, effect)
                    })
                    Spacer(Modifier.width(8.dp))
                    Text(effect)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun performHapticFeedback(context: Context, effect: String) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val vibrationEffect = when (effect) {
        "CLICK" -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        "TEXT_HANDLE_MOVE" -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        else -> null
    }

    vibrationEffect?.let { vibrator.vibrate(it) }
}