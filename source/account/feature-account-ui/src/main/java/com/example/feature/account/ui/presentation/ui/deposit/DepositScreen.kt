package com.example.feature.account.ui.presentation.ui.deposit

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
internal fun DepositScreen(
    accountId: String,
    accountCurrencyType: String,
    viewModel: DepositViewModel,
    modifier: Modifier = Modifier,
    onNavigateToDetails: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is DepositScreenNews.NavigateToAccountDetailsAndUpdate -> onNavigateToDetails()

                is DepositScreenNews.ValidationError -> snackbarHostState
                    .showSnackbar(message = context.getString(R.string.withdraw_validation))

                is DepositScreenNews.ExceptionDuringRequest ->
                    snackbarHostState
                        .showSnackbar(message = "Произошла техническая ошибка, пожалуйста попробуйте позже")
            }
        }
    }

    DepositScreenInternal(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBackClicked = { viewModel.onBackButtonPressed(accountId) },
        onStartTransaction = {
            viewModel.depositOnAccount(
                depositAmount = state.moneyAmount.toDouble(),
                accountId = accountId,
                code = accountCurrencyType,
            )
        },
        onError = viewModel::sendErrorNews,
        onMoneyAmountChanged = viewModel::onUiStateChanged,
    )
}

@Composable
private fun DepositScreenInternal(
    modifier: Modifier,
    state: DepositScreenState,
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
        DepositContentScreen(
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
private fun DepositContentScreen(
    modifier: Modifier = Modifier,
    state: DepositScreenState,
    onMoneyAmountChanged: (String) -> Unit = {},
    onNavigateBackClicked: () -> Unit,
    onStartTransaction: () -> Unit,
    onError: () -> Unit,
) {
    when {
        state.successWithdraw -> SuccessDepositScreen(
            modifier = modifier,
            onNavigateBackClicked = onNavigateBackClicked,
        )

        else -> DepositContent(
            modifier = modifier,
            state = state,
            onMoneyAmountChanged = onMoneyAmountChanged,
            onError = onError,
            onStartTransaction = onStartTransaction,
        )
    }
}

@Composable
private fun DepositContent(
    modifier: Modifier,
    state: DepositScreenState,
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
            imageVector = ImageVector.vectorResource(R.drawable.deposit),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(24.dp))
        ComposeTextField(
            placeHolderText = "Введите сумму",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textFieldValue = state.moneyAmount,
            onValueChange = { onMoneyAmountChanged(it) }
        )
        when {
            state.isLoading -> DepositLoadingScreen()
            state.error -> onError()
            else -> DepositButton(
                onStartTransaction = onStartTransaction,
                state = state,
            )
        }
    }
}

@Composable
private fun ColumnScope.DepositButton(
    onStartTransaction: () -> Unit,
    state: DepositScreenState
) {
    AnimatedVisibility(true) {
        AccentButton(
            text = "Пополнить",
            modifier = Modifier.padding(top = 24.dp),
            onClick = onStartTransaction,
            isEnabled = state.moneyAmount.isNotBlank(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessDepositScreen(
    modifier: Modifier = Modifier,
    onNavigateBackClicked: () -> Unit = {},
) {
    AnimatedVisibility(true) {
        Column(
            modifier = modifier
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
                modifier = Modifier
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
fun DepositLoadingScreen() {
    AnimatedVisibility(true) {
        Column(
            modifier = Modifier.padding(top = 24.dp),
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