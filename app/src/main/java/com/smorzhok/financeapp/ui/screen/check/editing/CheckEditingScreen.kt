package com.smorzhok.financeapp.ui.screen.check.editing

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.NavigationState
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.screen.LocalAccountRepository
import com.smorzhok.financeapp.ui.screen.LocalTransactionRepository
import com.smorzhok.financeapp.ui.screen.check.CheckScreenViewModel
import com.smorzhok.financeapp.ui.screen.check.CheckScreenViewModelFactory
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon
import com.smorzhok.financeapp.ui.theme.Green

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckEditingScreen(navState: NavigationState) {
    val context = LocalContext.current
    val accountRepository = LocalAccountRepository.current
    val transactionRepository = LocalTransactionRepository.current
    val viewModel: CheckScreenViewModel = viewModel(
        factory = CheckScreenViewModelFactory(accountRepository, transactionRepository)
    )

    val checkState by viewModel.checkState.collectAsStateWithLifecycle()
    val dialogueMessage by viewModel.dialogueMessage.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.loadAccount(context)
    }

    CheckScreenAlertDialog(dialogueMessage, checkState, viewModel)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            BottomSheetEditCheckContent(
                onClose = { showBottomSheet = false },
                onCurrencySelected = { selectedCurrency ->
                    viewModel.currency.value = selectedCurrency
                }
            )
        }
    }

    val topBarContent = ScaffoldItem(
        textResId = R.string.my_account,
        trailingImageResId = R.drawable.check_mark,
        leadingImageResId = R.drawable.cross
    )

    Scaffold(
        topBar = {
            TopBarTextAndIcon(
                textResId = topBarContent.textResId,
                trailingImageResId = topBarContent.trailingImageResId,
                leadingImageResId = topBarContent.leadingImageResId,
                onLeadingClicked = { navState.navHostController.popBackStack() },
                onTrailingClicked = { viewModel.updateAccount(context) }
            )
        }
    ) { paddingValues ->
        CheckScreenContent(paddingValues, checkState, viewModel, onRetry = {
            viewModel.loadAccount(context)
        }, onCurrencyClick = { showBottomSheet = true })
    }
}

@Composable
private fun CheckScreenAlertDialog(
    dialogueMessage: String?,
    checkState: UiState<*>,
    viewModel: CheckScreenViewModel
) {
    if (dialogueMessage != null && checkState is UiState.Error) {
        AlertDialog(
            onDismissRequest = { viewModel.dialogueShown() },
            title = {
                Text(
                    text = stringResource(R.string.error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = dialogueMessage,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dialogueShown() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun CheckScreenContent(
    paddingValues: PaddingValues,
    checkState: UiState<*>,
    viewModel: CheckScreenViewModel,
    onRetry: () -> Unit,
    onCurrencyClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            when (checkState) {
                is UiState.Loading -> CheckScreenLoading()
                is UiState.Success -> CheckScreenForm(viewModel, onCurrencyClick)
                is UiState.Error -> CheckScreenError(
                    checkState.message.toString(),
                    onRetry
                )
            }
        }
    }
}

@Composable
private fun CheckScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CheckScreenError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        ErrorWithRetry(
            message = message,
            onRetryClick = onRetry,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CheckScreenForm(
    viewModel: CheckScreenViewModel,
    onCurrencyClick: () -> Unit
) {
    val nameState = viewModel.name
    val balanceState = viewModel.balance

    Column {
        ListItem(
            leadingContent = {
                Text(
                    text = stringResource(R.string.check_name),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingContent = {
                BasicTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            downDivider = true,
            onClick = {},
            backgroundColor = MaterialTheme.colorScheme.surface,
            verticalPadding = 16.0
        )
        ListItem(
            leadingContent = {
                Text(
                    text = stringResource(R.string.balance),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingContent = {
                BasicTextField(
                    value = balanceState.value,
                    onValueChange = { newValue ->
                        val sanitized = newValue.replace(",", ".")
                        if (sanitized.all { it.isDigit() || it == '.' }) {
                            balanceState.value = sanitized
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            downDivider = true,
            onClick = {},
            backgroundColor = MaterialTheme.colorScheme.surface,
            verticalPadding = 16.0
        )
        ListItem(
            leadingContent = {
                Text(
                    text = stringResource(R.string.currency),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = viewModel.currency.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painter = painterResource(R.drawable.more_vert_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically),
                        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                    )
                }
            },
            downDivider = true,
            onClick = onCurrencyClick,
            backgroundColor = MaterialTheme.colorScheme.surface,
            verticalPadding = 16.0
        )
    }
}
