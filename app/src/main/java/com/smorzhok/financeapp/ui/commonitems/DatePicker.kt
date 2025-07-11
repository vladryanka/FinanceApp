package com.smorzhok.financeapp.ui.commonitems

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    minDate: Long? = null,
    maxDate: Long? = null,
    context: Context
) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selected = LocalDate.of(year, month + 1, dayOfMonth)
            onDateSelected(selected)
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    )
    minDate?.let { datePickerDialog.datePicker.minDate = it }
    maxDate?.let { datePickerDialog.datePicker.maxDate = it }
    datePickerDialog.show()
}