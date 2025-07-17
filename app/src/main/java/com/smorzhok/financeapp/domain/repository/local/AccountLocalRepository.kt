package com.smorzhok.financeapp.domain.repository.local

import com.smorzhok.financeapp.domain.model.Account

interface AccountLocalRepository {
    suspend fun saveAccounts(accounts: List<Account>)
    suspend fun getCachedAccounts(): List<Account>
}