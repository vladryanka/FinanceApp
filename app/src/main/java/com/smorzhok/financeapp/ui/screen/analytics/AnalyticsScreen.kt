package com.smorzhok.financeapp.ui.screen.analytics

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.smorzhok.financeapp.ui.formatter.formatBackendTime
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticsScreen(
    viewModelFactory: ViewModelProvider.Factory,
    paddingValues: PaddingValues,
    isIncome: Boolean,
    onItemClicked: (Int) -> Unit
) {
    val viewModel: AnalyticsScreenViewModel = viewModel(factory = viewModelFactory)

    val transactionsState by viewModel.transactionList.collectAsStateWithLifecycle()

    var fromDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        loadTransactions(viewModel, fromDate, toDate, isIncome, context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (false) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        when (val state = transactionsState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorWithRetry(
                        message = state.message,
                        onRetryClick = {
                            loadTransactions(viewModel, fromDate, toDate, isIncome, context)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                val transactionsList = state.data
                val totalSum = transactionsList.sumOf { it.amount }
                val currency = if (transactionsList.isEmpty()) viewModel.currency.value else
                    transactionsList.get(0).currency
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
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.period_start),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {},//todo чипс,
                            downDivider = true,
                            onClick = {  },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = 16.0
                        )
                    }

                    item {
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.period_end),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {},//todo чипс,
                            downDivider = true,
                            onClick = {  },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = 16.0
                        )
                    }

                    item {
                        ListItem(
                            leadingContent = {
                                Text(
                                    stringResource(R.string.sum),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                Text(
                                    totalSumFormatted,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            downDivider = true,
                            onClick = {},
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = TODO(),
                        )
                    }
                    //todo канвас

                    itemsIndexed(transactionsList) { index, item ->
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
                                onItemClicked(item.id)
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

@RequiresApi(Build.VERSION_CODES.O)
private fun loadTransactions(
    viewModel: AnalyticsScreenViewModel,
    fromDate: LocalDate,
    toDate: LocalDate,
    isIncome: Boolean,
    context: Context
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fromStr = fromDate.format(formatter)
    val toStr = toDate.format(formatter)
    viewModel.loadTransactions(fromStr, toStr, isIncome, context)
}