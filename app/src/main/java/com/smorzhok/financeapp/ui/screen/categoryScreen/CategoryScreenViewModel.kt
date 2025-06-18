package com.smorzhok.financeapp.ui.screen.categoryScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.smorzhok.financeapp.ui.screen.commonItems.UiState
import com.smorzhok.financeapp.ui.screen.commonItems.retryWithBackoff
import kotlinx.coroutines.launch

class CategoryScreenViewModel(private val getCategoriesUseCase: GetCategoriesUseCase) :
    ViewModel() {
    private val _categoryState = MutableLiveData<UiState<List<Category>>>()
    val categoryState: LiveData<UiState<List<Category>>> = _categoryState

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            try {
                val categories = retryWithBackoff { getCategoriesUseCase() }
                _categoryState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _categoryState.value = UiState.Error(e.message)
            }
        }
    }
}