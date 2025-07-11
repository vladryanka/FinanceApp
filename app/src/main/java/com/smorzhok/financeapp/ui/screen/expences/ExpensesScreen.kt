package com.smorzhok.financeapp.ui.screen.expences

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.theme.Green
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    viewModelFactory: ViewModelProvider.Factory,
    paddingValues: PaddingValues,
    onExpenseClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val viewModel: ExpensesScreenViewModel = viewModel(factory = viewModelFactory)
    val expenseState by viewModel.expenseList.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val to = today.format(dateFormatter)
        viewModel.loadTransactions(to, to, context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (val state = expenseState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Success -> {
                val expensesList = state.data
                val totalPrice = expensesList.sumOf { it.amount }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                ) {
                    ListItem(
                        leadingContent = {
                            Text(
                                stringResource(R.string.total),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        trailingContent = {
                            val currency = if (expensesList.isEmpty()) viewModel.currency.value
                            else expensesList.get(0).currency

                            Text(
                                formatPrice(
                                    totalPrice.toDouble(),
                                    currency
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        downDivider = true,
                        onClick = { },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        verticalPadding = 16.0
                    )

                    LazyColumn {
                        itemsIndexed(expensesList) { index: Int, item: Transaction ->
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
                                                if (it.isNotBlank())
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.bodyMedium,
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
                                        Text(
                                            text = formatPrice(item.amount, item.currency),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                        Icon(
                                            painterResource(R.drawable.more_vert_icon),
                                            contentDescription = null,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                },
                                downDivider = true,
                                onClick = {
                                    onExpenseClicked(item.id)
                                },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 22.0
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                ErrorWithRetry(
                    message = state.message,
                    onRetryClick = {
                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val today = LocalDate.now()
                        val to = today.format(dateFormatter)
                        viewModel.loadTransactions(to, to, context)
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 14.dp
                ),
            shape = CircleShape,
            containerColor = Green,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_expense),
                tint = Color.White
            )
        }
    }
}


