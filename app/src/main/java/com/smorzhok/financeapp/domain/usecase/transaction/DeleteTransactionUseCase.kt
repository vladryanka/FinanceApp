package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

/*юзкейс для удаления транзакции по id*/
class DeleteTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(id: Int) {
        transactionRepository.deleteTransaction(id)
    }
}
