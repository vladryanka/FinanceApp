package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}