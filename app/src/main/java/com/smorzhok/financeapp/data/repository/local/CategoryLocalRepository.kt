package com.smorzhok.financeapp.data.repository.local

import com.smorzhok.financeapp.data.database.dao.CategoryDao
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toEntity
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.repository.local.CategoryLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryLocalRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryLocalRepository {

    override suspend fun saveCategories(categories: List<Category>) = withContext(Dispatchers.IO) {
        dao.insertCategories(categories.map { it.toEntity() })
    }

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        dao.getAllCategories().map { it.toDomain() }
    }
}