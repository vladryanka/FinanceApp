package com.smorzhok.financeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smorzhok.financeapp.data.database.entity.AccountEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    @Query("SELECT * FROM accounts WHERE isSynced = 0")
    suspend fun getUnsyncedAccounts(): List<AccountEntity>

    @Query("UPDATE accounts SET isSynced = 1 WHERE id = :accountId")
    suspend fun markAccountAsSynced(accountId: Int)

    @Update
    suspend fun updateAccount(account: AccountEntity)
}