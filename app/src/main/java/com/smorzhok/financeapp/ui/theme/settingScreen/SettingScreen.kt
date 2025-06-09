package com.smorzhok.financeapp.ui.theme.settingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Settings
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem

@Composable
fun SettingScreen(
    incomesList: List<Settings>?,
    paddingValues: PaddingValues,
    onSettingClicked: (Int) -> Unit
) {
    val incomesListState = remember { incomesList }
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
        if (incomesListState != null) {
            LazyColumn {
                item {
                    ListItem(
                        leadingContent = {
                            Text(
                                stringResource(R.string.light_dark_auto),
                                modifier = Modifier.padding(start = 16.dp),
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = checked,
                                onCheckedChange = { checked = it },
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        },
                        upDivider = false,
                        downDivider = true,
                        onClick = { },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    )
                }
                itemsIndexed(incomesListState) { index, item ->
                    ListItem(
                        leadingContent = {
                            Text(
                                text = stringResource(item.textLeadingResId),
                                fontSize = 24.sp,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        {
                            Icon(
                                painterResource(item.iconTrailingResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 16.dp),
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        },
                        upDivider = false,
                        downDivider = true,
                        onClick = { onSettingClicked(item.id) },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }
}