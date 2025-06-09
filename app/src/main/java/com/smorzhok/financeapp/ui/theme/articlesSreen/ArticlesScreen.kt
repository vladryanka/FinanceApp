package com.smorzhok.financeapp.ui.theme.articlesSreen

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
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Articles
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem

@Composable
fun ArticlesScreen(
    articlesList: List<Articles>?,
    paddingValues: PaddingValues,
    onArticleClicked: (Int) -> Unit
) {
    val articlesListState = remember { articlesList }

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
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            },
            trailingContent = {
                Icon(
                    painterResource(R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            },
            upDivider = false,
            downDivider = true,
            onClick = { },
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )

        if (articlesListState != null) {
            LazyColumn {
                itemsIndexed(articlesListState) { index, item ->
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
                        {},
                        upDivider = false,
                        downDivider = true,
                        onClick = { onArticleClicked(item.id) },
                        backgroundColor = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }


}