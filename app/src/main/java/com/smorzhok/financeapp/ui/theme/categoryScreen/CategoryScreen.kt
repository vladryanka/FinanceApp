package com.smorzhok.financeapp.ui.theme.categoryScreen

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.LocalFinanceRepository
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem

@Composable
fun CategoryScreen(
    paddingValues: PaddingValues,
    onArticleClicked: (Int) -> Unit
) {
    val financeRepository = LocalFinanceRepository.current
    val viewModel: CategoryScreenViewModel = viewModel(
        factory = CategoryScreenViewModelFactory(financeRepository)
    )

    val categoryListState = remember { viewModel.categoryDtoList.value }

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

        if (categoryListState != null) {
            LazyColumn {
                itemsIndexed(categoryListState) { index, item ->
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
                        {},
                        downDivider = true,
                        onClick = { onArticleClicked(item.id) },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        verticalPadding = 22.0
                    )
                }
            }
        }
    }


}