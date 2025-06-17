package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Account

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun updateAccounts(accounts: List<Account>)
}