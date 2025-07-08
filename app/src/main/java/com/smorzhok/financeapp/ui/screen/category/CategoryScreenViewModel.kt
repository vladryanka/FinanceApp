package com.smorzhok.financeapp.ui.screen.category

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


/*управление состоянием UI, связанным с загрузкой списка категорий*/
class CategoryScreenViewModel(private val getCategoriesUseCase: GetCategoriesUseCase) :
ViewModel() {
    private val _allCategories = MutableStateFlow<List<Category>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _categoryState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoryState: StateFlow<UiState<List<Category>>> = _categoryState

    val searchQuery: StateFlow<String> = _searchQuery

    val filteredCategories: StateFlow<List<Category>> = combine(
        _allCategories, _searchQuery
    ) { categories, query ->
        if (query.isBlank()) categories
        else categories.filter {
            it.textLeading.contains(query.trim(), ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadCategories(context: Context) {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _categoryState.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val categories =  getCategoriesUseCase()
                _allCategories.value = categories
                _categoryState.value = UiState.Success(categories)
            } catch (e: IOException) {
                _categoryState.value = UiState.Error(e.message)
            } catch (e: HttpException) {
                if(e.code() == 401){
                    _categoryState.value = UiState.Error("Не авторизован")
                } else{
                    _categoryState.value = UiState.Error(e.message ?: "server_error")
                }
            }
        }
    }
}
