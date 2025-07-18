package com.smorzhok.financeapp.data.repository.local

import com.smorzhok.financeapp.data.database.dao.AccountDao
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.mapper.toEntity
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.local.AccountLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountLocalRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountLocalRepository {

    override suspend fun saveAccounts(accounts: List<Account>) = withContext(Dispatchers.IO) {
        val entities = accounts.map { it.toEntity() }
        accountDao.insertAccounts(entities)
    }

    override suspend fun getCachedAccounts(): List<Account> = withContext(Dispatchers.IO) {
        accountDao.getAllAccounts().map { it.toDomain() }
    }

    override suspend fun updateAccount(account: Account,isSynced: Boolean) {
        accountDao.updateAccount(account.toEntity().copy(isSynced = isSynced))
    }
}