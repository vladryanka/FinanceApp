package com.smorzhok.financeapp.ui.screen.category

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.setting.performHapticFeedback

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CategoryScreen(
    hapticEffectType: String,
    viewModelFactory: ViewModelProvider.Factory,
    paddingValues: PaddingValues,
    onCategoryClicked: (Int) -> Unit
) {
    val viewModel: CategoryScreenViewModel = viewModel(factory = viewModelFactory)

    val categoryState by viewModel.categoryState.collectAsStateWithLifecycle()

    val filteredCategories by viewModel.filteredCategories.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
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
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.find_article),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
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
            onClick = { performHapticFeedback(context = context, effect = hapticEffectType) },
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
                LazyColumn {
                    itemsIndexed(filteredCategories) { _, item ->
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
                                            text = item.iconLeading
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
                            onClick = {
                                performHapticFeedback(context = context, effect = hapticEffectType)
                                onCategoryClicked(item.id)
                            },
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            verticalPadding = 22.0
                        )
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ErrorWithRetry(
                        message = state.error.toString(),
                        onRetryClick = { viewModel.loadCategories() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
