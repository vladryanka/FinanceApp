package com.smorzhok.financeapp.domain.usecase.account

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.AccountRepository
import javax.inject.Inject

/*юзкейс для получения данных об аккаунте*/
class GetAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(): List<Account> = accountRepository.getAccounts()
}
