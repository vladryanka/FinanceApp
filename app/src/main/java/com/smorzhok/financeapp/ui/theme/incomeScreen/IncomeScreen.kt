package com.smorzhok.financeapp.ui.theme.incomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Incomes
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@Composable
fun IncomeScreen(
    incomesList: List<Incomes>?,
    paddingValues: PaddingValues,
    onIncomeClicked: (Int) -> Unit
) {
    val incomesListState = remember { incomesList }

    val totalPrice = incomesListState?.sumOf { it.priceTrailing } ?: 0

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

        if (incomesListState != null) {
            LazyColumn {
                itemsIndexed(incomesListState) { index, item ->
                    ListItem(
                        leadingContent = {
                                Text(
                                    text = stringResource(item.textLeadingResId),
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    maxLines = 1
                                )

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
                        onClick = { onIncomeClicked(item.id) },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }



}