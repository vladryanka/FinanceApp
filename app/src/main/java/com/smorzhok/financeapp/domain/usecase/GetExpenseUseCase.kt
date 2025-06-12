package com.smorzhok.financeapp.domain.usecase

import com.smorzhok.financeapp.domain.model.ExpenseDto

class GetExpenseUseCase(private val financeRepository: FinanceRepository) {
    operator fun invoke(): List<ExpenseDto>? = financeRepository.getExpenses()
}