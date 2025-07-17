package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.retryWithBackoff
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.repository.CategoryRepository
import com.smorzhok.financeapp.domain.repository.local.CategoryLocalRepository
import com.smorzhok.financeapp.domain.repository.remote.CategoryRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/*Имплементация репозитория для данных о категориях*/
class CategoryRepositoryImpl @Inject constructor(
    private val remote: CategoryRemoteRepository,
    private val local: CategoryLocalRepository
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            val remoteCategories = retryWithBackoff {
                remote.getCategories()
            }
            local.saveCategories(remoteCategories)
            remoteCategories
        } catch (_: IOException) {
            local.getCategories()
        } catch (_: Exception) {
            local.getCategories()
        }
    }
}
