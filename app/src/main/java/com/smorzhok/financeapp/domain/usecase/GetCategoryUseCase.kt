package com.smorzhok.financeapp.domain.usecase

import com.smorzhok.financeapp.domain.model.CategoryDto

class GetCategoryUseCase(private val financeRepository: FinanceRepository) {
    operator fun invoke(): List<CategoryDto>? = financeRepository.getCategories()
}