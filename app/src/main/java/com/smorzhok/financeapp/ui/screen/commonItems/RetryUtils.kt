package com.smorzhok.financeapp.ui.screen.commonItems

import retrofit2.HttpException
import kotlinx.coroutines.delay
import java.io.IOException

suspend fun <T> retryWithBackoff(
    retries: Int = 3,
    delayMillis: Long = 2000,
    block: suspend () -> T
): T {
    repeat(retries - 1) {
        try {
            return block()
        } catch (e: HttpException) {
            if (e.code() != 500 && e.code() != 429) throw e
        } catch (e: IOException) {
        }
        delay(delayMillis)
    }
    return block()
}