package com.smorzhok.financeapp.data.repository.remote

import com.smorzhok.financeapp.data.mapper.toAccountUpdateRequest
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.remote.AccountRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRemoteRepositoryImpl @Inject constructor(
    private val api: FinanceApiService
) : AccountRemoteRepository {

    override suspend fun getAccounts(): List<Account> = withContext(Dispatchers.IO) {
        api.getAccountList().map { it.toDomain() }
    }

    override suspend fun updateAccount(account: Account) = withContext(Dispatchers.IO) {
        val request = account.toAccountUpdateRequest()
        api.updateAccount(account.id, request)
    }
}