package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository

class GetCurrencyUseCase(private val transactionRepository: TransactionRepository) {
    operator fun invoke() = transactionRepository.getCurrentCurrency()
}