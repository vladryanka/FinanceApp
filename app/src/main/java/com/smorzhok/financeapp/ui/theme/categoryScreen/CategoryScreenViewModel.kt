package com.smorzhok.financeapp.ui.theme.categoryScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.model.CategoryDto
import com.smorzhok.financeapp.domain.usecase.FinanceRepository
import com.smorzhok.financeapp.domain.usecase.GetCategoryUseCase

class CategoryScreenViewModel(financeRepository: FinanceRepository): ViewModel() {
    private val getCategoryUseCase = GetCategoryUseCase(financeRepository)
    private val initialCategoryDtoLists = getCategoryUseCase()
    private val _categoryDtoList= MutableLiveData<List<CategoryDto>>(initialCategoryDtoLists)
    val categoryDtoList: LiveData<List<CategoryDto>> get() = _categoryDtoList

}