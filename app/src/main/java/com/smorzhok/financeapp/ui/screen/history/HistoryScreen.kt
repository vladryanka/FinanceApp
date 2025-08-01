package com.smorzhok.financeapp.ui.screen.history

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.showDatePicker
import com.smorzhok.financeapp.ui.formatter.formatBackendTime
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.setting.performHapticFeedback
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    hapticEffectType: String,
    viewModelFactory: ViewModelProvider.Factory,
    isIncome: Boolean,
    onHistoryItemClicked: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    val viewModel: HistoryScreenViewModel = viewModel(factory = viewModelFactory)

    val displayDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")

    var fromDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    LaunchedEffect(fromDate, toDate, isIncome) {
        loadHistory(viewModel, fromDate, toDate, isIncome)
    }

    val historyListState by viewModel.historyList.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (false) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        when (val state = historyListState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorWithRetry(
                        message = state.error.toString(),
                        onRetryClick = {
                            performHapticFeedback(context = context, effect = hapticEffectType)
                            loadHistory(viewModel, fromDate, toDate, isIncome)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                val historyList = state.data
                val totalSum = historyList.sumOf { it.amount }
                val currency = if (historyList.isEmpty()) viewModel.currency.value else
                    historyList[0].currency
                val totalSumFormatted = formatPrice(totalSum, currency)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                ) {
                    item {
                        GreenInfoItemClickable(
                            leadingTextResId = R.string.start,
                            trailingText = fromDate.format(displayDateFormatter),
                            onClick = {
                                performHapticFeedback(context = context, effect = hapticEffectType)
                                val maxDateMillis = toDate.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli()
                                showDatePicker(
                                    initialDate = fromDate,
                                    onDateSelected = { selected ->
                                        if (!selected.isAfter(toDate)) {
                                            fromDate = selected
                                        }
                                    },
                                    maxDate = maxDateMillis,
                                    context = context
                                )
                            },
                            isDivider = true
                        )
                    }

                    item {
                        GreenInfoItemClickable(
                            leadingTextResId = R.string.end,
                            trailingText = toDate.format(displayDateFormatter),
                            onClick = {
                                val minDateMillis = fromDate.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli()
                                showDatePicker(
                                    initialDate = toDate,
                                    onDateSelected = { selected ->
                                        if (!selected.isBefore(fromDate) && !selected.isAfter(
                                                LocalDate.now()
                                            )
                                        ) {
                                            toDate = selected
                                        }
                                    },
                                    minDate = minDateMillis,
                                    context = context
                                )
                            },
                            isDivider = true
                        )
                    }

                    item {
                        GreenInfoItemClickable(
                            leadingTextResId = R.string.sum,
                            trailingText = totalSumFormatted,
                            onClick = {
                                performHapticFeedback(
                                    context = context,
                                    effect = hapticEffectType
                                )
                            },
                            isDivider = false
                        )
                    }

                    itemsIndexed(historyList) { index, item ->
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
                                            text = item.categoryEmoji,
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
                                            text = item.categoryName,
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                        item.comment?.let {
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
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                    ) {
                                        Text(
                                            text = formatPrice(item.amount, item.currency) +
                                                    "\n" + formatBackendTime(item.time),
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
                                performHapticFeedback(context = context, effect = hapticEffectType)
                                onHistoryItemClicked(item.id)
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 10.5
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GreenInfoItemClickable(
    leadingTextResId: Int,
    trailingText: String,
    onClick: () -> Unit,
    isDivider: Boolean
) {
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
                text = trailingText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickable { onClick() }
            )
        },
        downDivider = isDivider,
        onClick = onClick,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        verticalPadding = 15.5
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun loadHistory(
    viewModel: HistoryScreenViewModel,
    fromDate: LocalDate,
    toDate: LocalDate,
    isIncome: Boolean
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fromStr = fromDate.format(formatter)
    val toStr = toDate.format(formatter)
    viewModel.loadHistory(fromStr, toStr, isIncome)
}
