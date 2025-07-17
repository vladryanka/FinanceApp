package com.smorzhok.financeapp.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smorzhok.financeapp.data.model.dto.account.AccountDto
import com.smorzhok.financeapp.data.model.dto.account.AccountHistoryResponse
import com.smorzhok.financeapp.data.model.dto.account.AccountUpdateRequest
import com.smorzhok.financeapp.data.model.dto.category.CategoryDto
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionDto
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionRequest
import com.smorzhok.financeapp.data.model.dto.transaction.TransactionResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FinanceApiService {

    @GET("accounts")
    suspend fun getAccountList(): List<AccountDto>

    //будет использоваться, нужная ручка
    @GET("accounts/{id}/history")
    suspend fun getAccountHistoryById(@Path("id") id: Int): List<AccountHistoryResponse>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Int,
        @Body request: AccountUpdateRequest
    ): Response<AccountDto>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body request: TransactionRequest
    ): Response<TransactionDto>

    @GET("transactions/{id}")
    suspend fun getTransactionsById(
        @Path("id") id: Int,
    ): TransactionDto

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): Response<TransactionResponse>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") from: String,
        @Query("endDate") to: String
    ): List<TransactionDto>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int):Response<Unit>
}

/*синглтон для создания сервиса и связи с сетью*/
object FinanceApi {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val authInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val tokenInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(requestBuilder.build())
    }

    private const val TIMEOUT = 60L

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val service: FinanceApiService by lazy {
        retrofit.create(FinanceApiService::class.java)
    }
}
