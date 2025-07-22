package com.smorzhok.financeapp.ui.screen.commonComposable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.screen.setting.performHapticFeedback

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BottomSheetContent(
    onClose: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    itemsList: List<Pair<String, Int>>,
    hapticEffectType: String
) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        LazyColumn {
            itemsIndexed(itemsList) { index, (firstText, secRes) ->
                ListItem(
                    leadingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                firstText,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            if (secRes != 0) {
                                Text(
                                    text = stringResource(secRes),
                                    modifier = Modifier.padding(start = 16.dp),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    },
                    trailingContent = {},
                    downDivider = true,
                    onClick = {
                        performHapticFeedback(context, hapticEffectType)
                        onCurrencySelected(firstText)
                        onClose()
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    verticalPadding = 24.0
                )
            }
        }
        ListItem(
            leadingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cross_in_circle),
                        tint = Color.White,
                        contentDescription = stringResource(R.string.cancel)
                    )
                    Text(
                        text = stringResource(R.string.cancel),
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            },
            trailingContent = {},
            downDivider = true,
            onClick = {
                performHapticFeedback(context, hapticEffectType)
                onClose
            },
            backgroundColor = MaterialTheme.colorScheme.error,
            verticalPadding = 23.5
        )
    }
}

