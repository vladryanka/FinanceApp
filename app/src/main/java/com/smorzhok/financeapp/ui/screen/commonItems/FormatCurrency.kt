package com.smorzhok.financeapp.ui.screen.commonItems

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
