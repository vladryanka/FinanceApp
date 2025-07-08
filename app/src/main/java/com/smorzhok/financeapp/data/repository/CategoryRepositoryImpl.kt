package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.mapToCategory
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.data.retryWithBackoff
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*Имплементация репозитория для данных о категориях*/
class CategoryRepositoryImpl(
    private val api: FinanceApiService
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        retryWithBackoff { api.getCategories().map { it.mapToCategory() } }
    }
}
