package com.smorzhok.financeapp.domain.repository.remote

import com.smorzhok.financeapp.domain.model.Category

interface CategoryRemoteRepository {
    suspend fun getCategories(): List<Category>
}