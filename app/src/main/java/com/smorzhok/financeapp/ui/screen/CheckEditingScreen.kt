package com.smorzhok.financeapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.model.ScaffoldItem
import com.smorzhok.financeapp.navigation.NavigationState
import com.smorzhok.financeapp.ui.commonitems.UiState
import com.smorzhok.financeapp.ui.screen.commonComposable.ErrorWithRetry
import com.smorzhok.financeapp.ui.screen.commonComposable.ListItem
import com.smorzhok.financeapp.ui.screen.commonComposable.TopBarTextAndIcon
import com.smorzhok.financeapp.ui.viewmodel.CheckScreenViewModel
import com.smorzhok.financeapp.ui.viewmodel.CheckScreenViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckEditingScreen(navState: NavigationState) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val accountRepository = LocalAccountRepository.current
    val viewModel: CheckScreenViewModel = viewModel(
        factory = CheckScreenViewModelFactory(accountRepository)
    )
    val checkState by viewModel.checkState.observeAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadAccount(context)
    }

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
            topBarContent.let {
                TopBarTextAndIcon(
                    it.textResId, trailingImageResId = it.trailingImageResId,
                    leadingImageResId = it.leadingImageResId, onLeadingClicked = {
                        navState.navHostController.popBackStack()
                    }, onTrailingClicked = {
                        viewModel.updateAccount()
                    })
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                when (val state = checkState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        val nameState = viewModel.name
                        val balanceState = viewModel.balance

                        Column {
                            ListItem(
                                leadingContent = {
                                    Text(
                                        text = stringResource(R.string.check_name),
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
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
                                onClick = { },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 16.0
                            )
                            ListItem(
                                leadingContent = {
                                    Text(
                                        text = stringResource(R.string.balance),
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
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
                                onClick = { },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 16.0
                            )
                            ListItem(
                                leadingContent = {
                                    Text(
                                        text = stringResource(R.string.currency),
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = viewModel.currency.value,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Icon(
                                            painterResource(R.drawable.more_vert_icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                                .align(Alignment.CenterVertically),
                                            tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                        )
                                    }
                                },
                                downDivider = true,
                                onClick = { showBottomSheet = true },
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                verticalPadding = 16.0
                            )
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ErrorWithRetry(
                                message = state.message,
                                onRetryClick = { viewModel.loadAccount(context) },
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    null -> {}
                }
            }
        }
    }


}