package com.smorzhok.financeapp.ui.screen.analytics

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.domain.model.AnalyticsCategory
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionsUseCase
import com.smorzhok.financeapp.ui.commonitems.ErrorList
import com.smorzhok.financeapp.ui.commonitems.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.round

class AnalyticsScreenViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _analyticsCategory =
        MutableStateFlow<UiState<List<AnalyticsCategory>>>(UiState.Loading)
    val analyticsCategory: StateFlow<UiState<List<AnalyticsCategory>>> get() = _analyticsCategory

    val currency = mutableStateOf("")

    fun loadCategories(from: String, to: String, isIncome: Boolean) {
        viewModelScope.launch {
            _analyticsCategory.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _analyticsCategory.value = UiState.Error(ErrorList.NoAccount)
                    return@launch
                }

                val accountId = accounts.first().id
                val transactions = getTransactionsUseCase(accountId, from, to)
                    .filter { it.isIncome == isIncome }
                    .sortedBy { it.time }

                currency.value = transactions.firstOrNull()?.currency ?: "RUB"

                val categories = getCategoriesUseCase()
                val totalSum = transactions.sumOf { it.amount }

                val grouped = transactions.groupBy { it.categoryId }

                val spendings = grouped.mapNotNull { (categoryId, txList) ->
                    val category = categories.find { it.id == categoryId } ?: return@mapNotNull null
                    val sum = txList.sumOf { it.amount }
                    val percent = if (totalSum != 0.0) round(sum / totalSum * 100).toInt() else 0

                    AnalyticsCategory(
                        categoryName = category.textLeading,
                        categoryIcon = category.iconLeading,
                        totalAmount = sum,
                        percent = percent
                    )
                }.sortedByDescending { it.totalAmount }

                _analyticsCategory.value = UiState.Success(spendings)

            } catch (e: HttpException) {
                e.printStackTrace()
                _analyticsCategory.value = when (e.code()) {
                    401 -> UiState.Error(ErrorList.NotAuthorized)
                    else -> UiState.Error(ErrorList.ServerError)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                _analyticsCategory.value = UiState.Error(ErrorList.NoInternet)
            } catch (e: Exception) {
                e.printStackTrace()
                _analyticsCategory.value = UiState.Error(ErrorList.ServerError)
            }
        }
    }

}