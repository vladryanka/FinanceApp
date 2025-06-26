package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository

/*юзкейс для получения списка транзакций по id аккаунта с учетом временного периода*/
class GetTransactionsUseCase(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(accountId: Int, from: String, to: String) =
        transactionRepository.getTransactions(accountId, from, to)
}
