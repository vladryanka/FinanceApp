package com.smorzhok.financeapp.ui.screen.categoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.smorzhok.financeapp.LocalCategoryRepository
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.ui.screen.commonItems.ListItem
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.theme.Green

@Composable
fun CategoryScreen(
    paddingValues: PaddingValues,
    onCategoryClicked: (Int) -> Unit
) {
    val categoryRepository = LocalCategoryRepository.current
    val viewModel: CategoryScreenViewModel = viewModel(
        factory = CategoryScreenViewModelFactory(categoryRepository)
    )

    val categoryState by viewModel.categoryState.observeAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadCategories(context)
    }

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
                    stringResource(R.string.find_article),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            },
            trailingContent = {
                Icon(
                    painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            },
            downDivider = true,
            onClick = { },
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            verticalPadding = 16.0
        )

        when (val state = categoryState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val categories = (categoryState as UiState.Success<List<Category>>).data
                LazyColumn {
                    itemsIndexed(categories) { _, item ->
                        ListItem(
                            leadingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(24.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.secondary,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.iconLeading,
                                            color = Color(0xFFFCE4EB)
                                        )
                                    }
                                    Text(
                                        text = item.textLeading,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1
                                    )
                                }
                            },
                            trailingContent = {},
                            downDivider = true,
                            onClick = { onCategoryClicked(item.id) },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 22.0
                        )
                    }
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
                                viewModel.loadCategories(context)
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
}