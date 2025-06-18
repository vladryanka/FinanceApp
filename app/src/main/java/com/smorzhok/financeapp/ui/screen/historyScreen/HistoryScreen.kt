package com.smorzhok.financeapp.ui.screen.historyScreen

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.LocalAccountRepository
import com.smorzhok.financeapp.LocalTransactionRepository
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.commonItems.ListItem
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.screen.commonItems.formatBackendTime
import com.smorzhok.financeapp.ui.screen.commonItems.formatPrice
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    isIncome: Boolean,
    onHistoryItemClicked: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    val transactionRepository = LocalTransactionRepository.current
    val accountRepository = LocalAccountRepository.current
    val viewModel: HistoryScreenViewModel = viewModel(
        factory = HistoryScreenViewModelFactory(transactionRepository, accountRepository)
    )

    val displayDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy")
    val backendDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var fromDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    fun loadHistory() {
        val fromStr = fromDate.format(backendDateFormatter)
        val toStr = toDate.format(backendDateFormatter)
        viewModel.loadHistory(fromStr, toStr, isIncome, context)
    }

    LaunchedEffect(fromDate, toDate, isIncome) {
        loadHistory()
    }

    val historyListState by viewModel.historyList.observeAsState()

    fun showDatePicker(
        initialDate: LocalDate,
        onDateSelected: (LocalDate) -> Unit,
        minDate: Long? = null,
        maxDate: Long? = null
    ) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(selected)
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )
        minDate?.let { datePickerDialog.datePicker.minDate = it }
        maxDate?.let { datePickerDialog.datePicker.maxDate = it }
        datePickerDialog.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (historyListState == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        when (val state = historyListState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                val errorText = when (state.message) {
                    "no_internet" -> stringResource(R.string.no_internet_connection)
                    else -> stringResource(R.string.error)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = {
                                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val today = LocalDate.now()
                                val from = today.withDayOfMonth(1).format(dateFormatter)
                                val to = today.format(dateFormatter)
                                viewModel.loadHistory(from, to, isIncome, context)
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }
            }

            is UiState.Success -> {
                val historyList = state.data
                val totalSum = historyList.sumOf { it.amount }
                val totalSumFormatted = formatPrice(totalSum)

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
                                val maxDateMillis = toDate.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli()
                                showDatePicker(
                                    initialDate = fromDate,
                                    onDateSelected = { selected ->
                                        if (!selected.isAfter(toDate)) {
                                            fromDate = selected
                                        }
                                    },
                                    maxDate = maxDateMillis
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
                                        if (!selected.isBefore(fromDate) && !selected.isAfter(LocalDate.now())) {
                                            toDate = selected
                                        }
                                    },
                                    minDate = minDateMillis
                                )
                            },
                            isDivider = true
                        )
                    }

                    item {
                        GreenInfoItemClickable(
                            leadingTextResId = R.string.sum,
                            trailingText =totalSumFormatted,
                            onClick = {},
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
                                            text = formatPrice(item.amount) +
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
                                onHistoryItemClicked(item.id)
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 10.5
                        )
                    }
                }
            }

            else -> {}
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
@Preview
@Composable
fun HistoryPreview() {
    FinanceAppTheme {
        HistoryScreen(true, {}, PaddingValues(50.dp))
    }
}