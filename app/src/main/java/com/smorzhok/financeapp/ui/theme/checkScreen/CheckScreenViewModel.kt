package com.smorzhok.financeapp.ui.theme.checkScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.model.CheckDto
import com.smorzhok.financeapp.domain.usecase.FinanceRepository
import com.smorzhok.financeapp.domain.usecase.GetCheckUseCase

class CheckScreenViewModel(
    financeRepository: FinanceRepository
) : ViewModel() {
    private val checkUseCase = GetCheckUseCase(financeRepository)
    private val initialChecksList = checkUseCase()

    private val _checksList = MutableLiveData<List<CheckDto>>(initialChecksList)
    val checksList: LiveData<List<CheckDto>> get() = _checksList
}