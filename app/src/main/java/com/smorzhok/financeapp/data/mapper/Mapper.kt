package com.smorzhok.financeapp.data.mapper

import com.smorzhok.financeapp.data.model.AccountBrief
import com.smorzhok.financeapp.data.model.AccountDto
import com.smorzhok.financeapp.data.model.CategoryDto
import com.smorzhok.financeapp.data.model.TransactionDto
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.model.Transaction

fun TransactionDto.toDomain(): Transaction = Transaction(
    id = id,
    accountId = account.id.toString(),
    categoryEmoji = category.emoji,
    categoryName = category.name,
    isIncome = category.isIncome,
    amount = amount.toDoubleOrNull() ?: 0.0,
    time = transactionDate,
    comment = comment
)

fun Account.toAccountBrief(): AccountBrief = AccountBrief(
    id = id.toInt(),
    name = name,
    balance = balance.toString(),
    currency = currency
)

fun AccountDto.toDomain(): Account = Account(
    id = this.id,
    name = this.name,
    balance = this.balance.toDouble(),
    currency = this.currency
)

fun CategoryDto.mapToCategory(): Category{
    return Category(
        id = this.id,
        iconLeading = this.emoji,
        textLeading = this.name,
        isIncome = this.isIncome
    )
}