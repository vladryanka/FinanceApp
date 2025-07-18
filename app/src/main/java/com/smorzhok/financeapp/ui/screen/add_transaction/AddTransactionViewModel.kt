package com.smorzhok.financeapp.ui.screen.add_transaction

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.model.Transaction
import com.smorzhok.financeapp.domain.usecase.account.GetAccountUseCase
import com.smorzhok.financeapp.domain.usecase.category.GetCategoriesUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.CreateTransactionUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.DeleteTransactionUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.GetTransactionByIdUseCase
import com.smorzhok.financeapp.domain.usecase.transaction.UpdateTransactionUseCase
import com.smorzhok.financeapp.ui.commonitems.ErrorList
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.isNetworkAvailable
import com.smorzhok.financeapp.ui.formatter.combineDateTimeToIsoUtc
import com.smorzhok.financeapp.ui.formatter.extractDateAndTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class AddTransactionViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    private val _categoryList = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoryList: StateFlow<UiState<List<Category>>> = _categoryList

    private val _account = MutableStateFlow<UiState<Account>>(UiState.Loading)
    val account: StateFlow<UiState<Account>> = _account

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    var selectedTime by mutableStateOf(LocalTime.now())
        private set

    var comment by mutableStateOf("")
        private set
    var editableAccountName by mutableStateOf("")
        private set
    var editableAmount by mutableStateOf("0")
        private set

    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    fun onBalanceChanged(newBalance: String) {
        editableAmount = newBalance
    }

    fun onCategoryNameSelected(name: String) {
        selectedCategory = _categoryList.value.let { state ->
            if (state is UiState.Success) {
                state.data.find { it.textLeading == name }
            } else null
        }
    }

    private val _dialogueMessage = MutableStateFlow<Pair<DialogueType, String>?>(null)
    val dialogueMessage: StateFlow<Pair<DialogueType, String>?> = _dialogueMessage

    fun dialogueShown() {
        _dialogueMessage.value = null
    }

    fun loadAccount() {
        viewModelScope.launch {
            _account.value = UiState.Loading
            _categoryList.value = UiState.Loading

            try {
                val accounts = getAccountUseCase()
                if (accounts.isEmpty()) {
                    _account.value = UiState.Error(ErrorList.NoAccount)
                    return@launch
                }
                val firstAccount = accounts.first()
                _account.value = UiState.Success(firstAccount)
                editableAccountName = firstAccount.name
                val cat = getCategoriesUseCase()
                _categoryList.value = UiState.Success(cat)
                selectedCategory = cat[0]

            } catch (e: IOException) {
                e.printStackTrace()
                _account.value = UiState.Error(ErrorList.NoInternet)
            } catch (e: HttpException) {
                e.printStackTrace()
                if (e.code() == 401) {
                    _account.value = UiState.Error(ErrorList.NotAuthorized)
                } else {
                    _account.value = UiState.Error(ErrorList.ServerError)
                }
            }
        }
    }

    fun updateTransaction(transactionId: Int, context: Context) {
        viewModelScope.launch {
            val acc = (account.value as? UiState.Success)?.data ?: return@launch
            val cat = selectedCategory ?: return@launch

            try {
                val transaction = Transaction(
                    id = transactionId,
                    accountId = acc.id.toString(),
                    categoryId = cat.id,
                    categoryName = cat.textLeading,
                    currency = acc.currency,
                    categoryEmoji = "",
                    isIncome = true,
                    amount = editableAmount.toDouble(),
                    time = combineDateTimeToIsoUtc(
                        selectedDate.toString(),
                        selectedTime.toString()
                    ),
                    comment = comment
                )
                updateTransactionUseCase(transaction)
                _dialogueMessage.value =
                    DialogueType.SUCCESS to context.getString(R.string.account_updated_successfully)
            } catch (e: Exception) {
                Log.d("Doing", e.message.toString())
                _dialogueMessage.value =
                    DialogueType.ERROR to context.getString(R.string.network_error)
            }
        }
    }

    fun loadTransactionForEdit(transactionId: Int) {
        viewModelScope.launch {

            try {
                val transaction = getTransactionByIdUseCase(transactionId)
                if (transaction != null) {
                    editableAccountName = transaction.name
                    editableAmount = transaction.amount.toString()
                    selectedCategory = transaction.category
                    _account.value = UiState.Success(
                        Account(
                            id = transaction.accountId,
                            name = transaction.name,
                            balance = transaction.amount.toDouble(),
                            currency = transaction.currency
                        )
                    )
                    val (date, time) = extractDateAndTime(transaction.dateTime)
                    selectedDate = date
                    selectedTime = time
                }

                val categories = getCategoriesUseCase()
                _categoryList.value = UiState.Success(categories)

            } catch (e: IOException) {
                e.printStackTrace()
                _account.value = UiState.Error(ErrorList.NoInternet)
            } catch (e: HttpException) {
                e.printStackTrace()
                _account.value = if (e.code() == 401) {
                    UiState.Error(ErrorList.NotAuthorized)
                } else {
                    UiState.Error(ErrorList.ServerError)
                }
            }
        }
    }

    fun onCommentChanged(value: String) {
        comment = value
    }

    fun onDateSelected(value: LocalDate) {
        selectedDate = value
    }

    fun onTimeSelected(value: LocalTime) {
        selectedTime = value
    }

    fun createTransaction(context: Context) {
        viewModelScope.launch {
            val acc = (account.value as? UiState.Success)?.data ?: return@launch
            val cat = selectedCategory ?: return@launch

            try {

                val timeFormatted = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                val transaction = Transaction(
                    id = Random.nextInt(1, Int.MAX_VALUE),
                    accountId = acc.id.toString(),
                    categoryId = cat.id,
                    categoryName = cat.textLeading,
                    currency = acc.currency,
                    categoryEmoji = "",
                    isIncome = true,
                    amount = editableAmount.toDouble(),
                    time = combineDateTimeToIsoUtc(
                        selectedDate.toString(),
                        timeFormatted.toString()
                    ),
                    comment = comment
                )
                createTransactionUseCase(transaction)
                _dialogueMessage.value =
                    DialogueType.SUCCESS to context.getString(R.string.account_updated_successfully)
            } catch (e: Exception) {
                Log.d("Doing", e.message.toString())
                _dialogueMessage.value =
                    DialogueType.ERROR to context.getString(R.string.network_error)
            }
        }
    }

    fun deleteTransaction(id: Int, context: Context) {
        viewModelScope.launch {
            if (!isNetworkAvailable(context)) {
                _dialogueMessage.value =
                    DialogueType.ERROR to context.getString(R.string.network_error)
                return@launch
            }
            try {
                deleteTransactionUseCase(id)
                //поллинг
                repeat(3) { attempt ->
                    delay(1000L)
                    val result = runCatching { getTransactionByIdUseCase(id) }
                    if (result.isFailure) {
                        _dialogueMessage.value =
                            DialogueType.SUCCESS to context.getString(R.string.account_updated_successfully)
                        _isDeleting.value = false
                        return@launch
                    }
                }

                _dialogueMessage.value =
                    DialogueType.ERROR to context.getString(R.string.network_error)
            } catch (e: Exception) {
                Log.d("Doing", e.message.toString())
                _dialogueMessage.value =
                    DialogueType.ERROR to context.getString(R.string.network_error)
            }
        }
    }

}