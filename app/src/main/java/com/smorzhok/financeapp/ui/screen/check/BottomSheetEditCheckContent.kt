package com.smorzhok.financeapp.ui.screen.check

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem

@Composable
fun BottomSheetEditCheckContent(
    onClose: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val currencyList = listOf<Pair<String, Int>>(
            "₽" to R.string.russian_rub,
            "$" to R.string.american_dollar,
            "€" to R.string.euro
        )

        LazyColumn {
            itemsIndexed(currencyList) { index, (symbol, nameRes) ->
                ListItem(
                    leadingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                symbol,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = stringResource(nameRes),
                                modifier = Modifier.padding(start = 16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    },
                    trailingContent = {},
                    downDivider = true,
                    onClick = {
                        onCurrencySelected(symbol)
                        onClose()
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    verticalPadding = 23.5
                )
            }
        }
        ListItem(
            leadingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cross_in_circle),
                        tint = Color.White,
                        contentDescription = stringResource(R.string.cancel)
                    )
                    Text(
                        text = stringResource(R.string.cancel),
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            },
            trailingContent = {},
            downDivider = true,
            onClick = onClose,
            backgroundColor = MaterialTheme.colorScheme.error,
            verticalPadding = 23.5
        )
    }
}
