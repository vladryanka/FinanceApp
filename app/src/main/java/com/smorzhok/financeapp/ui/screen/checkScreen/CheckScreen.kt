package com.smorzhok.financeapp.ui.screen.checkScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.smorzhok.financeapp.LocalAccountRepository
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.commonItems.ListItem
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.screen.commonItems.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.screen.commonItems.formatPrice
import com.smorzhok.financeapp.ui.theme.Green

@Composable
fun CheckScreen(
    paddingValues: PaddingValues,
    onCheckClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val accountRepository = LocalAccountRepository.current
    val viewModel: CheckScreenViewModel = viewModel(
        factory = CheckScreenViewModelFactory(accountRepository)
    )

    val checkState by viewModel.checkState.observeAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadAccount(context)
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
                                        Text(
                                            text = stringResource(R.string.money_icon),
                                            color = Color(0xFFFCE4EB)
                                        )
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
                                        text = formatPrice(check.balance),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    EndIcon(
                                        Modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterVertically)
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
                                    EndIcon(
                                        Modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            },
                            downDivider = true,
                            onClick = { onCheckClicked(check.id) },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            verticalPadding = 16.0
                        )
                    }
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
                                    viewModel.loadAccount(context)
                                },
                                modifier = Modifier.padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }
                }

                null -> {}
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

@Composable
private fun EndIcon(modifier: Modifier) {
    Icon(
        painterResource(R.drawable.more_vert_icon),
        contentDescription = null,
        modifier = modifier,
        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
    )
}
