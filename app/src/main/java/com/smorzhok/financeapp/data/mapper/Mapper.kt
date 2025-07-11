package com.smorzhok.financeapp.data.mapper

import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.data.model.account.AccountDto
import com.smorzhok.financeapp.data.model.account.AccountUpdateRequest
import com.smorzhok.financeapp.data.model.category.CategoryDto
import com.smorzhok.financeapp.data.model.transaction.TransactionDto
import com.smorzhok.financeapp.data.model.transaction.TransactionRequest
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.model.TransactionEdit

fun TransactionDto.toDomain(): Transaction = Transaction(
    id = id,
    accountId = account.id.toString(),
    categoryId = category.id,
    categoryEmoji = category.emoji,
    currency = account.currency,
    categoryName = category.name,
    isIncome = category.isIncome,
    amount = this@toDomain.amount.toDoubleOrNull() ?: 0.0,
    time = transactionDate,
    comment = comment
)

fun Transaction.toTransactionRequest(): TransactionRequest = TransactionRequest(
    accountId = this.accountId.toInt(),
    categoryId = this.categoryId,
    amount = this.amount.toString(),
    transactionDate = this.time,
    comment = this.comment
)

fun TransactionDto.toTransactionEdit() = TransactionEdit(
    accountId = this.account.id,
    currency = this.account.currency,
    name = this.account.name.toString(),
    amount = this.amount.toString(),
    category = this.category.mapToCategory(),
    dateTime = this.transactionDate,
    comment = this.comment
)

fun Account.toAccountUpdateRequest() = AccountUpdateRequest(
    name = this.name,
    balance = this.balance.toString(),
    currency = this.currency
)

fun AccountDto.toDomain(): Account = Account(
    id = this.id,
    name = if (this.name.isBlank()) R.string.my_article.toString() else this.name, // реализовано так, тк мультисчет не предусмотрен
    balance = this.balance.toDouble(),
    currency = this.currency
)

fun CategoryDto.mapToCategory(): Category {
    return Category(
        id = this.id,
        iconLeading = this.emoji,
        textLeading = this.name,
        isIncome = this.isIncome
    )
}
