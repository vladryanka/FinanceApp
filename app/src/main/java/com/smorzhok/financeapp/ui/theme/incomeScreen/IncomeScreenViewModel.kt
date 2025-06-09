package com.smorzhok.financeapp.ui.theme.incomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Incomes

class IncomeScreenViewModel: ViewModel() {
    private val initialIncomesList = mutableListOf<Incomes>()
        .apply {
            repeat(2) {
                add(
                    Incomes(
                        id = it,
                        textLeadingResId = R.string.products_placeholder,
                        iconTrailingResId = R.drawable.more_vert_icon,
                        priceTrailing = 50000
                    )
                )
            }
        }

    private val _incomesList= MutableLiveData<List<Incomes>>(initialIncomesList)
    val incomesList: LiveData<List<Incomes>> get() = _incomesList
}