package com.smorzhok.financeapp.domain.usecase.account

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.AccountRepository

class UpdateAccountsUseCase(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(accounts: List<Account>) {
        accountRepository.updateAccounts(accounts)
    }
}