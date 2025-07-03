package com.smorzhok.financeapp.ui.screen.incomes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.LocalAccountRepository
import com.smorzhok.financeapp.ui.screen.LocalTransactionRepository
import com.smorzhok.financeapp.ui.theme.Green
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IncomeScreen(
    paddingValues: PaddingValues,
    onIncomeClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val transactionRepository = LocalTransactionRepository.current
    val accountRepository = LocalAccountRepository.current
    val viewModel: IncomeScreenViewModel = viewModel(
        factory = IncomeScreenViewModelFactory(transactionRepository, accountRepository)
    )

    val incomeState by viewModel.incomeList.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val to = today.format(dateFormatter)
        viewModel.loadIncomes(to, to, context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (val state = incomeState) {
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
                            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val today = LocalDate.now()
                            val to = today.format(dateFormatter)
                            viewModel.loadIncomes(to, to, context)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                val incomesList = state.data
                val totalPrice = incomesList.sumOf { it.amount }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
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
                            val currency = if (incomesList.isEmpty()) viewModel.currency.value
                            else incomesList.get(0).currency
                            Text(
                                formatPrice(totalPrice, currency),
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
                        itemsIndexed(incomesList) { index, item ->
                            ListItem(
                                leadingContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.categoryName,
                                            style = MaterialTheme.typography.bodyLarge,
                                        )

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
                                    onIncomeClicked(item.id)
                                },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 23.0
                            )
                        }
                    }
                }
            }

            null -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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