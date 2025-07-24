package com.smorzhok.financeapp.domain.repository.remote

import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.network.dto.account.AccountDto
import retrofit2.Response

interface AccountRemoteRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun updateAccount(account: Account): Response<AccountDto>
}