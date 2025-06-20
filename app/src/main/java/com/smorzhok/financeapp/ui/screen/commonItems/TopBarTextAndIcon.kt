package com.smorzhok.financeapp.ui.screen.commonItems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme
import com.smorzhok.financeapp.ui.theme.Green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTextAndIcon(
    textResId: Int,
    leadingImageResId: Int?,
    trailingImageResId: Int?,
    onTrailingClicked: () -> Unit,
    onLeadingClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(textResId),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            leadingImageResId?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .clickable { onLeadingClicked() },
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.8f)
                )
            }
        },
        actions = {
            trailingImageResId?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 18.dp)
                        .clickable { onTrailingClicked() },
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.8f)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Green
        )
    )
}

@Preview
@Composable
fun TopBarTextAndIconPreview() {
    FinanceAppTheme {
        TopBarTextAndIcon(R.string.expenses_today, R.drawable.refresh, R.drawable.refresh, {}, {})
    }
}