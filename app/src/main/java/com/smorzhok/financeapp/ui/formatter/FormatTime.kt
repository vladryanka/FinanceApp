package com.smorzhok.financeapp.ui.formatter

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatBackendTime(time: String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm/dd.MM")
    val zonedDateTime = ZonedDateTime.parse(time, inputFormatter)
    return zonedDateTime.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractDateAndTime(time: String): Pair<LocalDate, LocalTime> {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val zonedDateTime = ZonedDateTime.parse(time, inputFormatter)

    val date = zonedDateTime.toLocalDate()
    val timeOnly = zonedDateTime.toLocalTime()

    return date to timeOnly
}

@RequiresApi(Build.VERSION_CODES.O)
fun combineDateTimeToIsoUtc(dateStr: String, timeStr: String): String {
    val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"))

    val zonedDateTime = ZonedDateTime.of(date, time, ZoneOffset.UTC)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return zonedDateTime.format(formatter)
}
