package com.example.feature.account.ui.presentation.ui.withdraw

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.account.ui.R
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun WithdrawScreen(
    accountId: String,
    currentBalance: String,
    viewModel: WithdrawViewModel,
    modifier: Modifier = Modifier,
    onNavigateToDetails: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is WithdrawScreenNews.NavigateToAccountDetailsAndUpdate -> onNavigateToDetails()
                is WithdrawScreenNews.ValidationError -> snackbarHostState
                    .showSnackbar(message = context.getString(R.string.withdraw_validation))

                is WithdrawScreenNews.ExceptionDuringRequest ->
                    snackbarHostState
                        .showSnackbar(message = "Произошла техническая ошибка, пожалуйста попробуйте позже")

            }
        }
    }
    WithdrawScreenInternal(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBackClicked = { viewModel.onBackButtonPressed(accountId) },
        onStartTransaction = {
            viewModel.withdrawFromAccount(
                currentBalance = currentBalance.toDouble(),
                withdrawAmount = state.moneyAmount.toDouble(),
                accountId = accountId,
            )
        },
        onError = viewModel::sendErrorNews,
        onMoneyAmountChanged = viewModel::onUiStateChanged,
    )
}

@Composable
private fun WithdrawScreenInternal(
    modifier: Modifier,
    state: WithdrawScreenState,
    snackbarHostState: SnackbarHostState,
    onNavigateBackClicked: () -> Unit,
    onMoneyAmountChanged: (String) -> Unit = {},
    onStartTransaction: () -> Unit,
    onError: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        WithdrawContentScreen(
            state = state,
            modifier = Modifier.padding(padding),
            onNavigateBackClicked = onNavigateBackClicked,
            onStartTransaction = onStartTransaction,
            onError = onError,
            onMoneyAmountChanged = onMoneyAmountChanged,
        )
    }
}

@Composable
private fun WithdrawContentScreen(
    modifier: Modifier = Modifier,
    state: WithdrawScreenState,
    onMoneyAmountChanged: (String) -> Unit = {},
    onNavigateBackClicked: () -> Unit,
    onStartTransaction: () -> Unit,
    onError: () -> Unit,
) {
    when {
        state.successWithdraw -> {
            SuccessWithdrawScreen(
                modifier = modifier,
                onNavigateBackClicked = onNavigateBackClicked,
            )
        }

        else -> WithdrawContentScreen(
            modifier = modifier,
            state = state,
            onMoneyAmountChanged = onMoneyAmountChanged,
            onError = onError,
            onStartTransaction = onStartTransaction,
        )
    }
}

@Composable
private fun WithdrawContentScreen(
    modifier: Modifier,
    state: WithdrawScreenState,
    onMoneyAmountChanged: (String) -> Unit,
    onError: () -> Unit,
    onStartTransaction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.withdraw),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(24.dp))
        ComposeTextField(
            placeHolderText = "0 ₽",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textFieldValue = state.moneyAmount,
            onValueChange = { onMoneyAmountChanged(it) }
        )
        when {
            state.isLoading -> WithdrawLoadingScreen()
            state.error -> onError()
            else -> WithdrawButton(onStartTransaction, state)
        }

    }
}

@Composable
private fun ColumnScope.WithdrawButton(
    onStartTransaction: () -> Unit,
    state: WithdrawScreenState
) {
    AnimatedVisibility(true) {
        AccentButton(
            text = stringResource(R.string.withdraw),
            modifier = Modifier.padding(top = 24.dp),
            onClick = onStartTransaction,
            isEnabled = state.moneyAmount.isNotBlank(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessWithdrawScreen(
    modifier: Modifier = Modifier,
    onNavigateBackClicked: () -> Unit = {},
) {
    AnimatedVisibility(true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 64.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.success),
                contentDescription = ""
            )
            Text(
                text = "Транзакция прошла успешно!",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Colors.BlueDepression
                )
            )
            AccentButton(
                text = "Подтвердить",
                modifier = Modifier
                    .padding(top = 24.dp),
                onClick = onNavigateBackClicked,
            )
        }
    }
}

@Composable
fun WithdrawLoadingScreen(modifier: Modifier = Modifier) {
    AnimatedVisibility(true) {
        Column(
            modifier = modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                color = Colors.BlueDepression,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}