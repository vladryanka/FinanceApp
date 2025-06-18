package com.smorzhok.financeapp.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smorzhok.financeapp.data.model.AccountDto
import com.smorzhok.financeapp.data.model.response.AccountHistoryResponse
import com.smorzhok.financeapp.data.model.CategoryDto
import com.smorzhok.financeapp.data.model.TransactionDto
import com.smorzhok.financeapp.data.model.request.UpdateAccountsRequest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FinanceApiService {

    @GET("accounts")
    suspend fun getAccountList(): List<AccountDto>

    @GET("accounts/{id}/history")
    suspend fun getAccountHistoryById(@Path("id") id: Int,): List<AccountHistoryResponse>

    @POST("accounts")
    suspend fun updateAccounts(@Body request: UpdateAccountsRequest): Response<Unit>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") from: String,
        @Query("endDate") to: String
    ): List<TransactionDto>
}

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

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
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
