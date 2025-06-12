package com.smorzhok.financeapp.data.mapper

import com.smorzhok.financeapp.data.model.AccountResponse
import com.smorzhok.financeapp.data.model.Category
import com.smorzhok.financeapp.data.model.Transaction
import com.smorzhok.financeapp.domain.model.CategoryDto
import com.smorzhok.financeapp.domain.model.CheckDto
import com.smorzhok.financeapp.domain.model.ExpenseDto
import com.smorzhok.financeapp.domain.model.IncomeDto

fun Transaction.mapToExpensesDto(): ExpenseDto{
    return ExpenseDto(
        id = this.id,
        iconLeading = this.category.emoji,
        textLeading = this.category.name,
        commentLeading = this.comment,
        priceTrailing = this.amount.toDouble()
    )
}
fun Transaction.mapToIncomeDto(): IncomeDto{
    return IncomeDto(
        id = this.id,
        textLeading = this.category.name,
        priceTrailing = this.amount.toDouble()
    )
}

fun AccountResponse.mapToCheckDto(): CheckDto{
    return CheckDto(
        id = this.id,
        leadingIcon = this.incomeStats.emoji,
        balance = this.balance.toDouble(),
        currency = this.currency
    )
}

fun Category.mapToCategoryDto(): CategoryDto{
    return CategoryDto(
        id = this.id,
        iconLeading = this.emoji,
        textLeading = this.name
    )
}