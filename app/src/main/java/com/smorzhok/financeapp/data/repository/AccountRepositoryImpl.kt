package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.local.AccountLocalRepository
import com.smorzhok.financeapp.domain.repository.remote.AccountRemoteRepository
import com.smorzhok.financeapp.domain.repository.AccountRepository
import javax.inject.Inject

/*Имплементация репозитория для данных об аккаунте*/
class AccountRepositoryImpl @Inject constructor(
    private val remote: AccountRemoteRepository,
    private val local: AccountLocalRepository
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> {
        val accounts = remote.getAccounts()
        local.saveAccounts(accounts)
        return accounts
    }

    override suspend fun updateAccount(account: Account) {
        remote.updateAccount(account)
    }
}