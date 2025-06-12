package com.smorzhok.financeapp.ui.theme.commonItems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTextAndIcon(
    textResId: Int,
    imageResId: Int?,
    onClick: ()->Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = stringResource(textResId),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                if (imageResId!= null){
                    Icon(
                        painter = painterResource(imageResId),
                        contentDescription = stringResource(R.string.history),
                        modifier = Modifier
                            .padding(end = 18.dp)
                            .align(alignment = Alignment.CenterEnd)
                            .clickable{
                                onClick()
                            },
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.8f)

                    )
                }
            }

        }
    )
}

@Preview
@Composable
fun TopBarTextAndIconPreview() {
    FinanceAppTheme {
        TopBarTextAndIcon(R.string.expenses_today, R.drawable.refresh,{})
    }
}