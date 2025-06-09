package com.smorzhok.financeapp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.Expenses

class MainViewModel : ViewModel() {
    private val initialExpensesList = mutableListOf<Expenses>()
        .apply {
            repeat(15) {
               // add(
//                    Expenses(
//                        iconResId = R.drawable.emoji_placeholder,
//                        nameResId = R.string.products_placeholder,
//                        price = 100000
//                    )
         //       )
            }
        }

    private val _expensesList= MutableLiveData<List<Expenses>>(initialExpensesList)
    val expensesList: LiveData<List<Expenses>> get() = _expensesList

}