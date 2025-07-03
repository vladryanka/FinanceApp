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

fun formatCurrencySymbolToCode(symbol: String): String {
    return when (symbol.uppercase()) {
        "₽" -> "RUB"
        "$" -> "USD"
        "€" -> "EUR"
        "£" -> "GBP"
        "¥" -> "JPY"
        else -> symbol
    }
}
