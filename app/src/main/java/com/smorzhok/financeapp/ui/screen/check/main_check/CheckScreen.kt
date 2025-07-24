package com.smorzhok.financeapp.ui.screen.check.main_check

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.check.CheckScreenViewModel
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.graphics.DailyBarChart

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CheckScreen(
    viewModelFactory: ViewModelProvider.Factory,
    paddingValues: PaddingValues,
    onCheckClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val viewModel: CheckScreenViewModel = viewModel(
        factory = viewModelFactory
    )

    val checkState by viewModel.checkState.collectAsStateWithLifecycle()
    val dailyStats by viewModel.dailyStats.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAccount()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            when (val state = checkState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val check = state.data
                    val stats = dailyStats

                    Column {
                        ListItem(
                            leadingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(24.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.onSecondary,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = stringResource(R.string.money_icon))
                                    }
                                    Text(
                                        text = stringResource(R.string.balance),
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = formatPrice(check.balance, check.currency),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            downDivider = true,
                            onClick = { onCheckClicked(check.id) },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = 16.0
                        )

                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.currency),
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = formatCurrencyCodeToSymbol(check.currency),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            downDivider = true,
                            onClick = { onCheckClicked(check.id) },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = 16.0
                        )

                        if (stats.isNotEmpty()) {
                            DailyBarChart(stats = stats)
                        } else {
                            Text(
                                text = stringResource(R.string.no_data_for_graph),
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ErrorWithRetry(
                            message = state.error.toString(),
                            onRetryClick = { viewModel.loadAccount() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
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
            containerColor = MaterialTheme.colorScheme.primary,
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
