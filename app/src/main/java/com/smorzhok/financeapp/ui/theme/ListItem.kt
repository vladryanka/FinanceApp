package com.smorzhok.financeapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smorzhok.financeapp.R

@Composable
fun ListItem(
    leadingContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    upDivider: Boolean,
    downDivider: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    if (upDivider) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 23.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()

        trailingContent()

    }
    if (downDivider) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }

}

@Preview
@Composable
fun ListItemPreview() {
    FinanceAppTheme {
        //ListItem(R.string.settings, 100000, R.drawable.more_vert_icon)
    }
}