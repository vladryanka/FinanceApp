package com.smorzhok.financeapp.data.repository

import android.util.Log
import com.smorzhok.financeapp.data.mapper.toAccountUpdateRequest
import com.smorzhok.financeapp.data.mapper.toDomain
import com.smorzhok.financeapp.data.remote.FinanceApiService
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*Имплементация репозитория для данных об аккаунте*/
class AccountRepositoryImpl(
    private val api: FinanceApiService
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> = withContext(Dispatchers.IO) {
        api.getAccountList().map { it.toDomain() }
    }

    override suspend fun updateAccount(account: Account) { //Todo что с запросом? ответ другой
        val request = account.toAccountUpdateRequest()
        Log.d("Doing", "Запрос на сервер = " + request.balance)
        Log.d("Doing", account.id.toString())
        withContext(Dispatchers.IO) {
            val resp = api.updateAccount(account.id, request)
            Log.d("Doing", "Ответ с сервера = "+resp.body()?.toString())
        }
    }
}
