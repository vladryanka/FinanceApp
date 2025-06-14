package com.smorzhok.financeapp.ui.theme.historyScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.domain.model.HistoryDto

class HistoryScreenViewModel: ViewModel() {

    private val list = mutableListOf<HistoryDto>().apply {
        repeat(4) {
            add(
                HistoryDto(
                    it,
                    "РК",
                    "Ремонт квартиры",
                    "Ремонт - фурнитура для дверей",
                    100000.0,
                    "22:01"
                )
            )
        }
    }

    private val _historyList = MutableLiveData<List<HistoryDto>>(list)
    val historyList: LiveData<List<HistoryDto>> get() = _historyList
}