package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

/*юзкейс для создания транзакции - дохода/расхода*/
class CreateTransactionUseCase @Inject constructor(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) =
        transactionRepository.createTransaction(transaction)
}
