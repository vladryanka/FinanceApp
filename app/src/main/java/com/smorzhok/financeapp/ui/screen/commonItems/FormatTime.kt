package com.smorzhok.financeapp.ui.screen.commonItems

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatBackendTime(time: String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm/dd.MM")
    val zonedDateTime = ZonedDateTime.parse(time, inputFormatter)
    return zonedDateTime.format(outputFormatter)
}