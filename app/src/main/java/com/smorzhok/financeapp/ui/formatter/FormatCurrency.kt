package com.smorzhok.financeapp.ui.formatter

fun formatCurrencyCodeToSymbol(code: String): String {
    return when (code.uppercase()) {
        "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "JPY" -> "¥"
        else -> code
    }
}
