package com.smorzhok.financeapp.ui.formatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.NumberFormat
import java.util.Locale

@Composable
fun formatPrice(price: Double, currency: String): String {
    val formattedNumber = remember(price) {
        NumberFormat.getInstance(Locale("ru")).format(price)
    }
    return "$formattedNumber ${formatCurrencyCodeToSymbol(currency)}"
}
