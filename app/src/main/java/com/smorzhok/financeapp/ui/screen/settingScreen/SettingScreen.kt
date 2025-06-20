package com.smorzhok.financeapp.ui.screen.settingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Settings
import com.smorzhok.financeapp.ui.screen.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.Green

@Composable
fun SettingScreen(
    paddingValues: PaddingValues,
    onSettingClicked: (Int) -> Unit
) {
    val settingsList = remember { getSettingsList() }

    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        LazyColumn {
            item {
                ListItem(
                    leadingContent = {
                        Text(
                            stringResource(R.string.dark_theme),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    trailingContent = {
                        Box(
                            modifier = Modifier.padding(end = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Switch(
                                checked = checked,
                                onCheckedChange = { checked = it },
                                modifier = Modifier.size(32.dp),
                                colors = SwitchDefaults.colors(
                                    checkedBorderColor = Green,
                                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                                    checkedThumbColor = Green,
                                    checkedTrackColor = MaterialTheme.colorScheme.secondary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                )
                            )
                        }
                    },
                    downDivider = true,
                    onClick = { },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    verticalPadding = 16.0
                )
            }
            itemsIndexed(settingsList) { index, item ->
                ListItem(
                    leadingContent = {
                        Text(
                            text = stringResource(item.textLeadingResId),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                        )
                    },
                    {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(item.iconTrailingResId),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                    },
                    downDivider = true,
                    onClick = { onSettingClicked(item.id) },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    verticalPadding = 15.5
                )
            }
        }
    }
}
private fun getSettingsList(): List<Settings> = listOf(
    Settings(0, R.string.main_color, R.drawable.triangle_vert),
    Settings(1, R.string.sounds, R.drawable.triangle_vert),
    Settings(2, R.string.haptics, R.drawable.triangle_vert),
    Settings(3, R.string.password, R.drawable.triangle_vert),
    Settings(4, R.string.synchronizing, R.drawable.triangle_vert),
    Settings(5, R.string.language, R.drawable.triangle_vert),
    Settings(6, R.string.about_the_program, R.drawable.triangle_vert),
)

@Preview
@Composable
fun SettingPreview() {
    FinanceAppTheme {
        SettingScreen(
            paddingValues = PaddingValues(50.dp),
            onSettingClicked = {}
        )
    }
}