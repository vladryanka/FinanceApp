package com.smorzhok.financeapp.ui.theme.checkScreen

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Balance
import com.smorzhok.financeapp.domain.model.Currency
import com.smorzhok.financeapp.domain.model.OnCheckScreen
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@Composable
fun ChecksScreen(
    checkList: List<OnCheckScreen>?,
    paddingValues: PaddingValues,
    onCheckClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val checkListState = remember { checkList }

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
            if (checkListState != null) {
                LazyColumn {
                    itemsIndexed(checkListState) { index, item ->
                        when (item) {
                            is Balance -> {
                                ListItem(
                                    leadingContent = {
                                        Row {
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .size(24.dp)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.onSecondary,
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
                                                maxLines = 1,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    },
                                    {
                                        Row {
                                            Text(
                                                text = formatPrice(item.priceTrailing),
                                                fontSize = 24.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Icon(
                                                painterResource(item.iconTrailingResId),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .align(Alignment.CenterVertically),
                                                tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                            )
                                        }
                                    },
                                    upDivider = false,
                                    downDivider = true,
                                    onClick = { onCheckClicked(item.id) },
                                    backgroundColor = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            is Currency -> {
                                ListItem(
                                    leadingContent = {
                                        Text(
                                            text = stringResource(item.textLeadingResId),
                                            fontSize = 24.sp,
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                    },
                                    {
                                        Row {
                                            Text(
                                                text = stringResource(item.currencyTrailing),
                                                fontSize = 24.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Icon(
                                                painterResource(item.iconTrailingResId),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .align(Alignment.CenterVertically),
                                                tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                            )
                                        }
                                    },
                                    upDivider = false,
                                    downDivider = true,
                                    onClick = { onCheckClicked(item.id) },
                                    backgroundColor = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

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