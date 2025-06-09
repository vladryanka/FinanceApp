package com.smorzhok.financeapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

    Surface(
        modifier = Modifier.clickable{
            onClick()
        },
        color = backgroundColor
    ) {
        Column {
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
                    .padding( vertical = 23.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    leadingContent()
                }
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
    }
}

@Preview
@Composable
fun ListItemPreview() {
    FinanceAppTheme {
        ListItem(
            leadingContent = {
                Row {
                    Icon(
                        painterResource(R.drawable.emoji_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color(0xFFFCE4EB)
                    )
                    Text(
                        text = "Аренда квартиры",
                        fontSize = 24.sp,
                        maxLines = 1
                    )
                }
            },
            trailingContent = {
                Row {
                    Text(
                        text = "100 000 Р",
                        fontSize = 24.sp,
                    )
                    Icon(
                        painterResource(R.drawable.more_vert_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            },
            upDivider = false,
            downDivider = true,
            onClick = {},
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    }
}