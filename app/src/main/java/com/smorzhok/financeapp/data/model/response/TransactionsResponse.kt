package com.smorzhok.financeapp.data.model.response

import com.smorzhok.financeapp.data.model.Transaction

data class TransactionsResponse(
    val transactions: List<Transaction>
)