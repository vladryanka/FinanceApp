package com.smorzhok.financeapp.domain.usecase

import com.smorzhok.financeapp.domain.model.IncomeDto

class GetIncomeUseCase(private val financeRepository: FinanceRepository) {
    operator fun invoke(): List<IncomeDto>? = financeRepository.getIncomes()
}