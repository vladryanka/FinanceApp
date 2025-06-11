package com.smorzhok.financeapp.domain.usecase

import com.smorzhok.financeapp.domain.model.CheckDto

class GetCheckUseCase(private val financeRepository: FinanceRepository) {
    operator fun invoke(): List<CheckDto>? = financeRepository.getChecks()
}