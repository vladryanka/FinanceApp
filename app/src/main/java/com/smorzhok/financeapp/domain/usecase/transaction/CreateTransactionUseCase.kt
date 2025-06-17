package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.TransactionRepository

class CreateTransactionUseCase(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) =
        transactionRepository.createTransaction(transaction)
}