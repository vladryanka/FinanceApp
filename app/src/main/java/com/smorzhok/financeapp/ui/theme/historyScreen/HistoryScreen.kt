package com.smorzhok.financeapp.ui.theme.historyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@Composable
fun HistoryScreen(
    onHistoryItemClicked: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    val viewModel: HistoryScreenViewModel = viewModel()
    val historyList by viewModel.historyList.observeAsState(emptyList())
    val historyListState = remember { historyList }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            item {
                GreenInfoItem(R.string.start, R.string.start, true) // Заменить на рил данные
            }
            item {
                GreenInfoItem(R.string.end, R.string.end, true) // Заменить на рил данные
            }
            item {
                GreenInfoItem(R.string.sum, R.string.sum, false) // Заменить на рил данные
            }

            itemsIndexed(historyListState) { index, item ->
                ListItem(
                    leadingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.leadingIcon,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 10.sp
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .align(Alignment.CenterVertically),
                            ) {
                                Text(
                                    text = item.leadingName,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                item.leadingComment?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                            ) {
                                Text(
                                    text = formatPrice(item.trailingPrice) +
                                            "\n${item.trailingTime}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                painterResource(R.drawable.more_vert_icon),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    },
                    downDivider = true,
                    onClick = {
                        onHistoryItemClicked(item.id)
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    verticalPadding = 10.5
                )
            }
        }

    }
}

@Composable
private fun GreenInfoItem(leadingTextResId: Int, trailingTextResId: Int, isDivider: Boolean) {
    ListItem(
        leadingContent = {
            Text(
                text = stringResource(leadingTextResId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingContent = {
            Text(
                text = stringResource(trailingTextResId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

        },
        downDivider = isDivider,
        onClick = { },
        backgroundColor = MaterialTheme.colorScheme.secondary,
        verticalPadding = 15.5
    )
}

@Preview
@Composable
fun HistoryPreview() {
    FinanceAppTheme {
        HistoryScreen({}, PaddingValues(50.dp))
    }
}