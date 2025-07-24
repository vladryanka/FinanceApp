package com.smorzhok.financeapp.ui.screen.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.data.model.colors.AppColorTheme

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ColorSelectionScreen(
    hapticEffectType: String,
    padding: PaddingValues,
    selectedColor: AppColorTheme,
    onColorSelected: (AppColorTheme) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding.calculateTopPadding()+8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.choose_main_color),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(16.dp))

        AppColorTheme.entries.forEach { colorTheme ->
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .background(
                        color = colorTheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
                    .then(
                        if (colorTheme == selectedColor) Modifier else Modifier
                    )
                    .clickable {
                        performHapticFeedback(context = context, effect = hapticEffectType)
                        onColorSelected(colorTheme)
                    }
            ) {
                Text(
                    text = colorTheme.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}