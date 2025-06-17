package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository

class DeleteTransactionUseCase(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(id: Int) {
        transactionRepository.deleteTransaction(id)
    }
}