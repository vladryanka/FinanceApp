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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.smorzhok.financeapp.domain.Expenses
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    expensesList: List<Expenses>?,
    paddingValues: PaddingValues,
    onExpenseClicked: (Int) -> Unit
) {
    val expensesListState = remember { expensesList }

    val totalPrice = expensesListState?.sumOf { it.priceTrailing } ?: 0

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
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingContent = {
                Text(
                    formatPrice(totalPrice),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            upDivider = false,
            downDivider = true,
            onClick = { },
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )

        if (expensesListState != null) {
            LazyColumn {
                itemsIndexed(expensesListState) { index, item ->
                    ListItem(
                        leadingContent = {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .size(24.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painterResource(item.iconLeadingResId),
                                        contentDescription = null,
                                        tint = Color(0xFFFCE4EB)
                                    )
                                }
                                Text(
                                    text = stringResource(item.textLeadingResId),
                                    fontSize = 24.sp,
                                    maxLines = 1
                                )
                            }
                        },
                        {
                            Row {
                                Text(
                                    text = formatPrice(item.priceTrailing),
                                    fontSize = 24.sp,
                                )
                                Icon(
                                    painterResource(item.iconTrailingResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        },
                        upDivider = false,
                        downDivider = true,
                        onClick = { onExpenseClicked(item.id) },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExpensesScreenPreview() {
    FinanceAppTheme {
        val expensesList = mutableListOf<Expenses>()
            .apply {
                repeat(15) {
                    add(
                        Expenses(

                            id = it,
                            iconLeadingResId = R.drawable.emoji_placeholder,
                            textLeadingResId = R.string.products_placeholder,
                            iconTrailingResId = R.drawable.more_vert_icon,
                            priceTrailing = 100000
                        )
                    )
                }
            }
        ExpensesScreen(
            expensesList,
            paddingValues = PaddingValues(50.dp),
            onExpenseClicked = { 1 }
        )

    }
}

