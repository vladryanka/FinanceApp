package com.smorzhok.financeapp.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.MainViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Expenses

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: List<Expenses>, paddingValues: PaddingValues) {
    val expensesListState = remember { viewModel }
    val expenses = expensesListState ?: emptyList()

    val totalPrice = expenses.sumOf { it.price }
    Log.d("Doing", expenses.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row {
            Text(stringResource(R.string.total), color = MaterialTheme.colorScheme.onPrimary)
            Text("$totalPrice ₽")
        }

        LazyColumn {
            itemsIndexed(expenses) { index, item ->
                ListItem(
                    leadingContent = {
                        Row {
                            Icon(
                                painterResource(item.iconResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 16.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(item.iconResId),
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    },
                    {
                        Text(
                            text = stringResource(item.iconResId),
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Icon(
                            painterResource(item.iconResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 16.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )


                    },
                    upDivider = false,
                    downDivider = true,
                    onClick = { },
                    backgroundColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview
@Composable
fun ExpensesScreenPreview() {
    FinanceAppTheme {
        val viewModel = mutableListOf<Expenses>()
            .apply {
                repeat(15) {
                    add(
                        Expenses(
                            iconResId = R.drawable.emoji_placeholder,
                            nameResId = R.string.products_placeholder,
                            price = 100000
                        )
                    )
                }
            }
        ExpensesScreen(viewModel, PaddingValues(50.dp))
    }
}

