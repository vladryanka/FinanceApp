package com.smorzhok.financeapp.data.repository

import com.smorzhok.financeapp.data.mapper.toAccountBrief
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.model.account.UpdateAccountsRequest
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

    override suspend fun updateAccounts(accounts: List<Account>) {
        val request = UpdateAccountsRequest(
            accounts = accounts.map { it.toAccountBrief() }
        )
        api.updateAccounts(request)
    }
}
