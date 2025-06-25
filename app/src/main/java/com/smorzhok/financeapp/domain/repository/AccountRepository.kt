package com.smorzhok.financeapp.domain.repository

import com.smorzhok.financeapp.domain.model.Account

/*репозиторий для данных об аккаунте*/
interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun updateAccounts(accounts: List<Account>)
}
