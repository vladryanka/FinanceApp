package com.smorzhok.financeapp.ui.screen.category

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.commonitems.retryWithBackoff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/*управление состоянием UI, связанным с загрузкой списка категорий*/
class CategoryScreenViewModel(private val getCategoriesUseCase: GetCategoriesUseCase) :
ViewModel() {
    private val _categoryState = MutableLiveData<UiState<List<Category>>>()
    val categoryState: LiveData<UiState<List<Category>>> = _categoryState

    fun loadCategories(context: Context) {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _categoryState.value = UiState.Error("no_internet")
                return@launch
            }
            try {
                val categories = withContext(Dispatchers.IO) {
                    retryWithBackoff { getCategoriesUseCase() }
                }
                _categoryState.value = UiState.Success(categories)
            } catch (e: IOException) {
                e.printStackTrace()
                _categoryState.value = UiState.Error(e.message)
            } catch (e: HttpException) {
                e.printStackTrace()
                _categoryState.value = UiState.Error(e.message ?: R.string.server_error.toString())
            }
        }
    }
}
