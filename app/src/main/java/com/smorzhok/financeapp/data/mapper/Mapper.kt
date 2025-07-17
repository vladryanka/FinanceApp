package com.smorzhok.financeapp.data.mapper

import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.data.model.dto.account.AccountDto
import com.smorzhok.financeapp.data.model.dto.account.AccountUpdateRequest
import com.smorzhok.financeapp.data.model.dto.category.CategoryDto
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionDto
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionRequest
import com.smorzhok.financeapp.data.model.entity.AccountEntity
import com.smorzhok.financeapp.data.model.entity.CategoryEntity
import com.smorzhok.financeapp.data.model.entity.TransactionEntity
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
    comment = if (comment == "") null else comment
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

fun CategoryEntity.toDomain() = Category(
    id = this.id,
    iconLeading = this.iconLeading,
    textLeading = this.textLeading,
    isIncome = this.isIncome
)

fun Category.toEntity() = CategoryEntity(
    id = this.id,
    iconLeading = this.iconLeading,
    textLeading = this.textLeading,
    isIncome = this.isIncome
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    accountId = this.accountId,
    categoryId = categoryId,
    categoryEmoji = this.categoryEmoji,
    currency = this.currency,
    categoryName = this.categoryName,
    isIncome = this.isIncome,
    amount = this.amount,
    time = this.time,
    comment = if (comment == "") null else comment
)
fun Transaction.toEntity() = TransactionEntity(
    id = id,
    accountId = this.accountId,
    categoryId = categoryId,
    categoryEmoji = this.categoryEmoji,
    currency = this.currency,
    categoryName = this.categoryName,
    isIncome = this.isIncome,
    amount = this.amount,
    time = this.time,
    comment = if (comment == "") null else comment
)

fun AccountEntity.toDomain() = Account(
    id = this.id,
    name = this.name,
    balance = this.balance,
    currency = this.currency
)

fun Account.toEntity() = AccountEntity(
    id = this.id,
    name = this.name,
    balance = this.balance,
    currency = this.currency
)



