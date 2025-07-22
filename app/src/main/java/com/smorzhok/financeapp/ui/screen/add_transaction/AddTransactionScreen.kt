package com.smorzhok.financeapp.ui.screen.add_transaction

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.Account
import com.smorzhok.financeapp.domain.model.Category
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.NavigationState
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.commonitems.showDatePicker
import com.smorzhok.financeapp.ui.formatter.formatCurrencyCodeToSymbol
import com.smorzhok.financeapp.ui.screen.commonComposable.BottomSheetContent
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTransactionScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navState: NavigationState,
    transactionId: Int? = null,
    hapticEffectType: String
) {

    val viewModel: AddTransactionViewModel = viewModel(factory = viewModelFactory)

    val categoryListState by viewModel.categoryList.collectAsStateWithLifecycle()
    val accountState by viewModel.account.collectAsStateWithLifecycle()

    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current

    val topBarContent = ScaffoldItem(
        textResId = R.string.add_transaction,
        trailingImageResId = R.drawable.check_mark,
        leadingImageResId = R.drawable.cross,
        backgroundColor = MaterialTheme.colorScheme.primary
    )

    LaunchedEffect(viewModel) {
        if (transactionId != null) {
            viewModel.loadTransactionForEdit(transactionId)
        } else {
            viewModel.loadAccount()
        }
    }

    if (showCategoryBottomSheet && categoryListState is UiState.Success) {
        val categoryList = (categoryListState as UiState.Success<List<Category>>).data
        ModalBottomSheet(
            onDismissRequest = { showCategoryBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                itemsList = categoryList.map { it.textLeading to 0 },
                onCurrencySelected = {
                    viewModel.onCategoryNameSelected(it)
                },
                onClose = { showCategoryBottomSheet = false },
                hapticEffectType = hapticEffectType
            )
        }
    }

    val dialogueMessage by viewModel.dialogueMessage.collectAsStateWithLifecycle()
    CheckScreenAlertDialog(dialogueMessage, viewModel)

    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()

    if (isDeleting) { // для поллинга
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Scaffold(
        topBar = {
            TopBarTextAndIcon(
                textResId = topBarContent.textResId,
                trailingImageResId = topBarContent.trailingImageResId,
                leadingImageResId = topBarContent.leadingImageResId,
                onLeadingClicked = { navState.navHostController.popBackStack() },
                onTrailingClicked = {
                    if (transactionId == null) {
                        viewModel.createTransaction(context)
                    } else viewModel.updateTransaction(transactionId, context)
                },
                backgroundColor = topBarContent.backgroundColor
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (categoryListState is UiState.Success && accountState is UiState.Success) {
                val account = (accountState as UiState.Success<Account>).data
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val items = listOf(
                    Triple(R.string.check, account.name, true),
                    Triple(R.string.article, viewModel.selectedCategory?.textLeading ?: "", true),
                    Triple(R.string.sum, account.balance.toString(), false),
                    Triple(R.string.date, viewModel.selectedDate.format(dateFormatter), false),
                    Triple(R.string.time, viewModel.selectedTime.format(timeFormatter), false)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn {
                        itemsIndexed(items) { _, (title, value) ->
                            ListItem(
                                leadingContent = {
                                    Text(
                                        text = stringResource(title),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                trailingContent = {
                                    when (title) {
                                        R.string.check -> {
                                            Row {
                                                Text(
                                                    text = value,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Icon(
                                                    painterResource(R.drawable.more_vert_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.padding(start = 16.dp)
                                                )
                                            }
                                        }

                                        R.string.sum -> {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                BasicTextField(
                                                    value = viewModel.editableAmount,
                                                    onValueChange = viewModel::onBalanceChanged,
                                                    singleLine = true,
                                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.End
                                                    ),
                                                    modifier = Modifier
                                                        .padding(end = 4.dp)
                                                )

                                                Text(
                                                    text = formatCurrencyCodeToSymbol(account.currency),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        R.string.article -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = value,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Icon(
                                                    painterResource(R.drawable.more_vert_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.padding(start = 16.dp)
                                                )
                                            }
                                        }

                                        else -> {
                                            Text(
                                                text = value,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                },
                                downDivider = true,
                                onClick = {
                                    if (title == R.string.date) {
                                        val minDateMillis = LocalDate.now().minusMonths(1)
                                            .atStartOfDay(ZoneId.systemDefault()).toInstant()
                                            .toEpochMilli()

                                        showDatePicker(
                                            initialDate = viewModel.selectedDate,
                                            onDateSelected = {
                                                viewModel.onDateSelected(it)
                                            },
                                            minDate = minDateMillis,
                                            context = context
                                        )
                                    }
                                    if (title == R.string.article) {
                                        showCategoryBottomSheet = true
                                    }
                                },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 23.0
                            )
                        }
                    }

                    BasicTextField(
                        value = viewModel.comment,
                        onValueChange = viewModel::onCommentChanged,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 23.dp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (viewModel.comment.isBlank()) {
                                    Text(
                                        text = stringResource(R.string.enter_comment),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    if (transactionId != null) {
                        Button(
                            onClick = {
                                viewModel.deleteTransaction(transactionId, context)
                                navState.navHostController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                color = Color.White
                            )
                        }
                    }
                }
            } else if (categoryListState is UiState.Loading || accountState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val error = (categoryListState as? UiState.Error)?.error
                    ?: (accountState as? UiState.Error)?.error
                    ?: stringResource(R.string.unknown_error)

                ErrorWithRetry(
                    message = error.toString(),
                    onRetryClick = { viewModel.loadAccount() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CheckScreenAlertDialog(
    dialogueData: Pair<DialogueType, String>?,
    viewModel: AddTransactionViewModel
) {
    if (dialogueData != null) {
        val (type, message) = dialogueData
        val titleText = when (type) {
            DialogueType.ERROR -> stringResource(R.string.error)
            DialogueType.SUCCESS -> stringResource(R.string.success)
        }
        val titleColor = when (type) {
            DialogueType.ERROR -> MaterialTheme.colorScheme.error
            DialogueType.SUCCESS -> MaterialTheme.colorScheme.primary
        }

        AlertDialog(
            onDismissRequest = { viewModel.dialogueShown() },
            title = {
                Text(titleText, color = titleColor)
            },
            text = {
                Text(message, color = MaterialTheme.colorScheme.onSurface)
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dialogueShown() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.ok), color = Color.White)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

enum class DialogueType {
    ERROR,
    SUCCESS
}