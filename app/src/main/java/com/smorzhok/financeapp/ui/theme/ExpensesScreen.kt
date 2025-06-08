package com.smorzhok.financeapp.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.smorzhok.financeapp.MainViewModel
import com.smorzhok.financeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: MainViewModel, paddingValues: PaddingValues) {
    val expensesListState by viewModel.expensesList.observeAsState()
    val expenses = expensesListState ?: emptyList()

    val totalPrice = expenses.sumOf { it.price }
    Log.d("Doing",expenses.toString())

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
                    textResId = item.nameResId,
                    imageResId = item.iconResId,
                    price = item.price
                )
            }
        }
    }
}
