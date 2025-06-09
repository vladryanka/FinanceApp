package com.smorzhok.financeapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.smorzhok.financeapp.R

@Composable
fun formatPrice(price: Int): String {
    val priceFormat = stringResource(id = R.string.price)
    return String.format(priceFormat, price.toString())
}