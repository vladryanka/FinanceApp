package com.smorzhok.financeapp.domain.usecase.account

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.AccountRepository

/*юзкейс для обновления данных аккаунта*/
class UpdateAccountsUseCase(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(account: Account) {
        accountRepository.updateAccount(account)
    }
}
