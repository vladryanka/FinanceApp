package com.smorzhok.financeapp.domain.repository.local

import com.smorzhok.financeapp.domain.model.Category

interface CategoryLocalRepository {
    suspend fun saveCategories(categories: List<Category>)
    suspend fun getCategories(): List<Category>
}