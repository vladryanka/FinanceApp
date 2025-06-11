package com.smorzhok.financeapp.ui.theme.expenseScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.model.ExpenseDto
import com.smorzhok.financeapp.domain.usecase.FinanceRepository
import com.smorzhok.financeapp.domain.usecase.GetExpenseUseCase

class ExpensesScreenViewModel(financeRepository: FinanceRepository) : ViewModel() {
    private val getExpenseUseCase = GetExpenseUseCase(financeRepository)
    private val initialExpenseDtoLists = getExpenseUseCase()

    private val _expenseDtoList= MutableLiveData<List<ExpenseDto>?>(initialExpenseDtoLists)
    val expenseDtoList: LiveData<List<ExpenseDto>?> get() = _expenseDtoList

}