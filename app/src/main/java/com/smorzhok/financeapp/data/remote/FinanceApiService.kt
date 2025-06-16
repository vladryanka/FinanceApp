package com.smorzhok.financeapp.data.remote

import com.smorzhok.financeapp.data.model.request.CreateTransactionRequest
import com.smorzhok.financeapp.data.model.request.UpdateAccountsRequest
import com.smorzhok.financeapp.data.model.response.AccountsResponse
import com.smorzhok.financeapp.data.model.response.CategoryResponse
import com.smorzhok.financeapp.data.model.response.TransactionsResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit


interface FinanceApiService {

    @GET("account/list")
    suspend fun getAccountList(
    ): AccountsResponse

    @PUT("/account")
    suspend fun updateAccounts(@Body request: UpdateAccountsRequest): Response<Unit>

    @GET("category/list")
    suspend fun getCategories(): CategoryResponse

    @GET("/transaction/list")
    suspend fun getTransactions(
        @Query("from") from: Long,
        @Query("to") to: Long
    ): TransactionsResponse

    @POST("/transaction")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): Response<Unit>
}

object DeezerApi {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
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
