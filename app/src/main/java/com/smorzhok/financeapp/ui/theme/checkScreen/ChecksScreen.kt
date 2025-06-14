package com.smorzhok.financeapp.ui.theme.checkScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.CheckDto
import com.smorzhok.financeapp.ui.theme.commonItems.ListItem
import com.smorzhok.financeapp.ui.theme.commonItems.formatPrice

@Composable
fun ChecksScreen(
    check: CheckDto?,
    paddingValues: PaddingValues,
    onCheckClicked: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    val checkState = remember { check }

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
            if (checkState != null) {
                Column {
                    ListItem(
                        leadingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .size(24.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.onSecondary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = checkState.leadingIcon,
                                        color = Color(0xFFFCE4EB)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.balance),
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatPrice(checkState.balance),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                EndIcon(
                                    Modifier
                                        .padding(start = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        },
                        downDivider = true,
                        onClick = { onCheckClicked(checkState.id) },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        verticalPadding = 16.0
                    )
                    ListItem(
                        leadingContent = {
                            Text(
                                text = stringResource(R.string.currency),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = checkState.currency,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                EndIcon(
                                    Modifier
                                        .padding(start = 16.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        },
                        downDivider = true,
                        onClick = { onCheckClicked(checkState.id) },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        verticalPadding = 16.0
                    )
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

@Composable
private fun EndIcon(modifier: Modifier) {
    Icon(
        painterResource(R.drawable.more_vert_icon),
        contentDescription = null,
        modifier = modifier,
        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
    )
}
