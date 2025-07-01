package com.smorzhok.financeapp.data.mapper

import com.smorzhok.financeapp.data.model.account.AccountBrief
import com.smorzhok.financeapp.data.model.account.AccountDto
import com.smorzhok.financeapp.data.model.category.CategoryDto
import com.smorzhok.financeapp.data.model.transaction.TransactionDto
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
    name = "Мой счет", // реализовано так, тк мультисчет не предусмотрен
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
