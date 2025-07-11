package com.smorzhok.financeapp.domain.usecase.transaction

import com.smorzhok.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

/*юзкейс для получения списка транзакций по id аккаунта с учетом временного периода*/
class GetTransactionsUseCase @Inject constructor(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(accountId: Int, from: String, to: String) =
        transactionRepository.getTransactions(accountId, from, to)
}
