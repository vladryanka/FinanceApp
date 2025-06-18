package com.smorzhok.financeapp.ui.screen.incomeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.LocalAccountRepository
import com.smorzhok.financeapp.LocalTransactionRepository
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.commonItems.ListItem
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.screen.commonItems.formatPrice
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
    LaunchedEffect(Unit) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val to = today.format(dateFormatter)
        viewModel.loadIncomes(to, to)
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
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = {
                            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val today = LocalDate.now()
                            val to = today.format(dateFormatter)
                            viewModel.loadIncomes(to, to)
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.retry))
                    }
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
                            Text(
                                formatPrice(totalPrice),
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
                                            text = formatPrice(item.amount),
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
            containerColor = MaterialTheme.colorScheme.background,
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