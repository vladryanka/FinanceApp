package com.smorzhok.financeapp.ui.theme.incomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.model.IncomeDto
import com.smorzhok.financeapp.domain.usecase.FinanceRepository
import com.smorzhok.financeapp.domain.usecase.GetIncomeUseCase

class IncomeScreenViewModel(
    financeRepository: FinanceRepository
): ViewModel() {
    private val getIncomeUseCase = GetIncomeUseCase(financeRepository)
    private val initialIncomeDtoLists = getIncomeUseCase()

    private val _incomeDtoList= MutableLiveData<List<IncomeDto>>(initialIncomeDtoLists)
    val incomeDtoList: LiveData<List<IncomeDto>> get() = _incomeDtoList
}