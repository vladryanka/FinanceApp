package com.smorzhok.financeapp.ui.screen.analytics

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.toModel
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.showDatePicker
import com.smorzhok.financeapp.ui.formatter.formatLocalDateToMonthYear
import com.smorzhok.financeapp.ui.formatter.formatPrice
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.setting.performHapticFeedback
import com.smorzhok.financeapp.ui.theme.Green
import com.smorzhok.graphics.CategoryPieChart
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticsScreen(
    hapticEffectType: String,
    viewModelFactory: ViewModelProvider.Factory,
    paddingValues: PaddingValues,
    isIncome: Boolean
) {
    val viewModel: AnalyticsScreenViewModel = viewModel(factory = viewModelFactory)

    val categoriesState by viewModel.analyticsCategory.collectAsStateWithLifecycle()

    var fromDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }

    val context = LocalContext.current
    LaunchedEffect(fromDate, toDate, isIncome) {
        loadCategories(viewModel, fromDate, toDate, isIncome)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (false) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        when (val state = categoriesState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorWithRetry(
                        message = state.error.toString(),
                        onRetryClick = {
                            performHapticFeedback(context = context, effect = hapticEffectType)
                            loadCategories(viewModel, fromDate, toDate, isIncome)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                val categoryList = state.data
                val totalSum = categoryList.sumOf { it.totalAmount }
                val currency = viewModel.currency.value
                val totalSumFormatted = formatPrice(totalSum, currency)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        )
                ) {
                    item {
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.period_start),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                GreenChip(
                                    text = formatLocalDateToMonthYear(fromDate),
                                    onClick = {
                                        showDatePicker(
                                            initialDate = fromDate,
                                            onDateSelected = {
                                                fromDate = it
                                            },
                                            context = context
                                        )
                                    },
                                    context = context,
                                    hapticEffectType = hapticEffectType
                                )
                            },
                            downDivider = true,
                            onClick = {
                                performHapticFeedback(
                                    context = context,
                                    effect = hapticEffectType
                                )
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 4.0
                        )
                    }

                    item {
                        ListItem(
                            leadingContent = {
                                Text(
                                    text = stringResource(R.string.period_end),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                GreenChip(
                                    text = formatLocalDateToMonthYear(toDate),
                                    onClick = {
                                        showDatePicker(
                                            initialDate = toDate,
                                            onDateSelected = {
                                                toDate = it
                                            },
                                            minDate = fromDate.toEpochDay() * 24 * 60 * 60 * 1000,
                                            context = context
                                        )
                                    },
                                    context = context,
                                    hapticEffectType = hapticEffectType
                                )
                            },
                            downDivider = true,
                            onClick = {
                                performHapticFeedback(
                                    context = context,
                                    effect = hapticEffectType
                                )
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 4.0
                        )
                    }

                    item {
                        ListItem(
                            leadingContent = {
                                Text(
                                    stringResource(R.string.sum),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                Text(
                                    totalSumFormatted,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            downDivider = true,
                            onClick = {
                                performHapticFeedback(
                                    context = context,
                                    effect = hapticEffectType
                                )
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 16.0,
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val data = categoryList.map { it.toModel() }
                            CategoryPieChart(data = data,total = totalSum)
                        }
                    }

                    if (categoryList.isEmpty()) {
                        item {
                            Text(
                                stringResource(R.string.no_data_for_period),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize()
                            )
                        }
                    } else {
                        itemsIndexed(categoryList) { index, item ->
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
                                                text = item.categoryIcon,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 10.sp
                                            )
                                        }
                                        Text(
                                            text = item.categoryName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                                .align(Alignment.CenterVertically)
                                        )
                                    }
                                },
                                trailingContent = {

                                    Text(
                                        text = item.percent.toString() +
                                                "%\n" + formatPrice(item.totalAmount, currency),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                },
                                downDivider = true,
                                onClick = {
                                    performHapticFeedback(
                                        context = context,
                                        effect = hapticEffectType
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 10.5
                            )
                        }
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun loadCategories(
    viewModel: AnalyticsScreenViewModel,
    fromDate: LocalDate,
    toDate: LocalDate,
    isIncome: Boolean
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fromStr = fromDate.format(formatter)
    val toStr = toDate.format(formatter)
    viewModel.loadCategories(fromStr, toStr, isIncome)
}

@Composable
fun GreenChip(
    text: String,
    onClick: () -> Unit,
    hapticEffectType: String, context: Context
) {
    AssistChip(
        onClick = {
            performHapticFeedback(context = context, effect = hapticEffectType)
            onClick()
        },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }, shape = RoundedCornerShape(50),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Green,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = null
    )
}