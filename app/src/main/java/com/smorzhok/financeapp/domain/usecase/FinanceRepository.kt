package com.smorzhok.financeapp.domain.usecase

import com.smorzhok.financeapp.domain.model.CategoryDto
import com.smorzhok.financeapp.domain.model.CheckDto
import com.smorzhok.financeapp.domain.model.ExpenseDto
import com.smorzhok.financeapp.domain.model.IncomeDto

interface FinanceRepository {
    fun getExpenses(): List<ExpenseDto>?
    fun getIncomes(): List<IncomeDto>?
    fun getChecks(): List<CheckDto>?
    fun getCategories(): List<CategoryDto>?
}