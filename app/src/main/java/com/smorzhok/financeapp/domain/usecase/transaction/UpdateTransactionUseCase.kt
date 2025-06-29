package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.TransactionRepository

/*юзкейс для обновления транзакции*/
class UpdateTransactionUseCase(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.updateTransaction(transaction)
    }
}
