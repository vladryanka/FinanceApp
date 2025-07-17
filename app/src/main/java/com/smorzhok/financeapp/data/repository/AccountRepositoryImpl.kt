package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.local.AccountLocalRepository
import com.smorzhok.financeapp.domain.repository.remote.AccountRemoteRepository
import com.smorzhok.financeapp.domain.repository.AccountRepository
import java.io.IOException
import javax.inject.Inject

/*Имплементация репозитория для данных об аккаунте*/
class AccountRepositoryImpl @Inject constructor(
    private val remote: AccountRemoteRepository,
    private val local: AccountLocalRepository
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> {
        return try {
            val accounts = remote.getAccounts()
            local.saveAccounts(accounts)
            accounts
        } catch (_: IOException) {
            local.getCachedAccounts()
        }
    }

    override suspend fun updateAccount(account: Account) {
        remote.updateAccount(account)
    }
}