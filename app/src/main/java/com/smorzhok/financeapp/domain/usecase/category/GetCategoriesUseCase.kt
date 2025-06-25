package com.smorzhok.financeapp.domain.usecase.category

import com.smorzhok.financeapp.domain.repository.CategoryRepository

/*юзкейс для получения категорий*/
class GetCategoriesUseCase(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke() = categoryRepository.getCategories()
}
