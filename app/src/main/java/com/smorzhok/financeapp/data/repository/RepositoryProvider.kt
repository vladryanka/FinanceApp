package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.domain.usecase.FinanceRepository

object RepositoryProvider {

    private lateinit var financeRepository: FinanceRepository

    fun initialize() {
        financeRepository = FinanceRepositoryImpl()
    }

    fun getFinanceRepository(): FinanceRepository {
        if (!::financeRepository.isInitialized) {
            throw IllegalStateException("FinanceRepository is not initialized. Call initialize() first.")
        }
        return financeRepository
    }

}