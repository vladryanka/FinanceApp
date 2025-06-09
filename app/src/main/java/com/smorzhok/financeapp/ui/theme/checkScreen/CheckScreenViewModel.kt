package com.smorzhok.financeapp.ui.theme.checkScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Balance
import com.smorzhok.financeapp.domain.model.Currency
import com.smorzhok.financeapp.domain.model.OnCheckScreen

class CheckScreenViewModel : ViewModel() {
    private val initialChecksList = mutableListOf<OnCheckScreen>()
        .apply {
            add(
                Balance(
                    id = 1,
                    iconLeadingResId = R.drawable.emoji_placeholder,
                    textLeadingResId = R.string.balance,
                    iconTrailingResId = R.drawable.more_vert_icon,
                    priceTrailing = -670000
                )

            )
            add(
                Currency(
                    id = 2,
                    textLeadingResId = R.string.currency,
                    currencyTrailing = R.string.rub,
                    iconTrailingResId = R.drawable.more_vert_icon
                )
            )

        }

    private val _checksList = MutableLiveData<List<OnCheckScreen>>(initialChecksList)
    val checksList: LiveData<List<OnCheckScreen>> get() = _checksList
}