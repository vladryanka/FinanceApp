package com.smorzhok.financeapp.ui.theme.expenseScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Expenses

class ExpensesScreenViewModel : ViewModel() {
    private val initialExpensesList = mutableListOf<Expenses>()
        .apply {
            repeat(15) {
                add(
                    Expenses(
                        id = it,
                        iconLeadingResId = R.drawable.emoji_placeholder,
                        textLeadingResId = R.string.products_placeholder,
                        iconTrailingResId = R.drawable.more_vert_icon,
                        priceTrailing = 10000
                    )
                )
            }
        }

    private val _expensesList= MutableLiveData<List<Expenses>>(initialExpensesList)
    val expensesList: LiveData<List<Expenses>> get() = _expensesList

}