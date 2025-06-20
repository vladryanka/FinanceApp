package com.smorzhok.financeapp.ui.screen.categoryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.financeapp.domain.repository.CategoryRepository
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase

@Suppress("UNCHECKED_CAST")
class CategoryScreenViewModelFactory (
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryScreenViewModel::class.java)) {
            val getCategoriesUseCase = GetCategoriesUseCase(categoryRepository)
            return CategoryScreenViewModel(getCategoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}