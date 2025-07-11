package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(id: Int) =
        transactionRepository.getTransactionById(id)
}