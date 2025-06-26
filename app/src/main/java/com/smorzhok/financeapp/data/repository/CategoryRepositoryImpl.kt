package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.mapToCategory
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.repository.CategoryRepository

/*Имплементация репозитория для данных о категориях*/
class CategoryRepositoryImpl(
    private val api: FinanceApiService
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().map { it.mapToCategory() }
    }
}
