package com.smorzhok.financeapp.domain.usecase.category

import com.smorzhok.financeapp.domain.repository.CategoryRepository
import javax.inject.Inject

/*юзкейс для получения категорий*/
class GetCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke() = categoryRepository.getCategories()
}
