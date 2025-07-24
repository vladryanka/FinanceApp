package com.smorzhok.financeapp.data.repository.remote

import com.smorzhok.financeapp.data.mapper.mapToCategory
import com.smorzhok.financeapp.data.retryWithBackoff
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.repository.remote.CategoryRemoteRepository
import com.smorzhok.network.FinanceApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRemoteRepositoryImpl @Inject constructor(
    private val api: FinanceApiService
) : CategoryRemoteRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        retryWithBackoff {
            api.getCategories().map { it.mapToCategory() }
        }
    }
}