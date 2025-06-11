package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.mapToCategoryDto
import com.smorzhok.financeapp.data.mapper.mapToCheckDto
import com.smorzhok.financeapp.data.mapper.mapToExpensesDto
import com.smorzhok.financeapp.data.mapper.mapToIncomeDto
import com.smorzhok.financeapp.data.model.AccountBrief
import com.smorzhok.financeapp.data.model.AccountResponse
import com.smorzhok.financeapp.data.model.Category
import com.smorzhok.financeapp.data.model.StatItem
import com.smorzhok.financeapp.data.model.Transaction
import com.smorzhok.financeapp.domain.model.CategoryDto
import com.smorzhok.financeapp.domain.model.CheckDto
import com.smorzhok.financeapp.domain.model.ExpenseDto
import com.smorzhok.financeapp.domain.model.IncomeDto
import com.smorzhok.financeapp.domain.usecase.FinanceRepository

class FinanceRepositoryImpl : FinanceRepository {
    override fun getExpenses(): List<ExpenseDto>? {
        val initialExpensesList = mutableListOf<Transaction>()
            .apply {
                repeat(15) {
                    add(
                        Transaction(
                            id = it,
                            account = AccountBrief(
                                id = it,
                                name = "Основной счёт",
                                balance = "1000.00",
                                currency = "RUB"
                            ),
                            category = Category(
                                id = 1,
                                name = "Зарплата",
                                emoji = "💰",
                                isIncome = true
                            ),
                            amount = "500.00",
                            transactionDate = "2025-06-10T21:56:58.596Z",
                            comment = "Зарплата за месяц",
                            createdAt = "2025-06-10T21:56:58.596Z",
                            updatedAt = "2025-06-10T21:56:58.596Z"
                        )
                    )
                }
            }
        return initialExpensesList.map { it.mapToExpensesDto() }
    }

    override fun getIncomes(): List<IncomeDto>? {
        val initialIncomesList = mutableListOf<Transaction>()
            .apply {
                add(
                    Transaction(
                        id = 1,
                        account = AccountBrief(
                            id = 1,
                            name = "Основной счёт",
                            balance = "1000.00",
                            currency = "RUB"
                        ),
                        category = Category(
                            id = 1,
                            name = "Зарплата",
                            emoji = "💰",
                            isIncome = true
                        ),
                        amount = "500.00",
                        transactionDate = "2025-06-10T21:56:58.596Z",
                        comment = "Зарплата за месяц",
                        createdAt = "2025-06-10T21:56:58.596Z",
                        updatedAt = "2025-06-10T21:56:58.596Z"
                    )
                )
                add(
                    Transaction(
                        id = 2,
                        account = AccountBrief(
                            id = 2,
                            name = "Основной счёт",
                            balance = "1000.00",
                            currency = "RUB"
                        ),
                        category = Category(
                            id = 2,
                            name = "Подработка",
                            emoji = "💰",
                            isIncome = true
                        ),
                        amount = "100.00",
                        transactionDate = "2025-06-10T21:56:58.596Z",
                        comment = "Подработка за месяц",
                        createdAt = "2025-06-10T21:56:58.596Z",
                        updatedAt = "2025-06-10T21:56:58.596Z"
                    )
                )

            }
        return initialIncomesList.map { it.mapToIncomeDto() }
    }

    override fun getChecks(): List<CheckDto>? {
        val initialAccountList = mutableListOf<AccountResponse>()
            .apply {
                add(
                    AccountResponse(
                        id = 1,
                        name = "Основной счёт",
                        balance = "1000.00",
                        currency = "RUB",
                        incomeStats = StatItem(
                            categoryId = 1,
                            categoryName = "Зарплата",
                            emoji = "💰",
                            amount = "5000.00"
                        ),
                        expenseStats = StatItem(
                            categoryId = 1,
                            categoryName = "Зарплата",
                            emoji = "💰",
                            amount = "5000.00"
                        ),
                        createdAt = "2025-06-10T23:10:28.275Z",
                        updatedAt = "2025-06-10T23:10:28.275Z"
                    )
                )
            }
        return initialAccountList.map { it.mapToCheckDto() }
    }

    override fun getCategories(): List<CategoryDto> {
        val initialCategoryList = mutableListOf<Category>()
            .apply {
                repeat(7) {
                    add(
                        Category(
                            id = 1,
                            name = "Зарплата",
                            emoji = "💰",
                            isIncome = true
                        )
                    )
                }

            }
        return initialCategoryList.map { it.mapToCategoryDto() }
    }
}