package com.smorzhok.financeapp.ui.theme.expenseScreen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ExpenseDto
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    expenseDtoList: List<ExpenseDto>?,
    paddingValues: PaddingValues,
    onExpenseClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val expensesListState = remember { expenseDtoList }

    val totalPrice = expensesListState?.sumOf { it.priceTrailing } ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
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
                    Text(
                        formatPrice(totalPrice.toDouble()),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                downDivider = true,
                onClick = { },
                backgroundColor = MaterialTheme.colorScheme.secondary,
                verticalPadding = 16.0
            )

            if (expensesListState != null) {
                LazyColumn {
                    itemsIndexed(expensesListState) { index, item ->
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
                                            text = item.iconLeading,
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
                                            text = item.textLeading,
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                        item.commentLeading?.let {
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
                                        text = formatPrice(item.priceTrailing),
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
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = paddingValues.calculateTopPadding() + 14.dp
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

@Preview
@Composable
fun ExpensesScreenPreview() {
    FinanceAppTheme {
        val expenseDtoLists = mutableListOf<ExpenseDto>()
            .apply {
                repeat(15) {
                    add(
                        ExpenseDto(
                            id = it,
                            iconLeading = "💰",
                            textLeading = "Продукты",
                            priceTrailing = 100000.0,
                            commentLeading = "джек"
                        )
                    )
                }
            }
        ExpensesScreen(
            expenseDtoLists,
            paddingValues = PaddingValues(50.dp),
            onExpenseClicked = { 1 },
            {}
        )

    }
}

