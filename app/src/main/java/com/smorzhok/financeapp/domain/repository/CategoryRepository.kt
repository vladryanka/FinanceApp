package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Category

/*репозиторий для данных о категориях*/
interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
