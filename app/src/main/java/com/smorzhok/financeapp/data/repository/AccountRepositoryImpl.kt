package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.toAccountBrief
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.AccountRepository

/*Имплементация репозитория для данных об аккаунте*/
class AccountRepositoryImpl(
    private val api: FinanceApiService
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> {
        return api.getAccountList().map { it.toDomain() }
    }

    override suspend fun updateAccount(account: Account) {
        val request = account.toAccountBrief()

        api.updateAccount(account.id, request)
    }
}
