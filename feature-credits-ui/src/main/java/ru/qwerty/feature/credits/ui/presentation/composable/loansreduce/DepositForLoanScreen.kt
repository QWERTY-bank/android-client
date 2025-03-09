package ru.qwerty.feature.credits.ui.presentation.composable.loansreduce

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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.feature.credits.ui.R

/**
 * @author y.yangel
 */
@Composable
internal fun DepositForLoanScreen(
    accountId: Int,
    creditId: String,
    accountMoneyAmount: String,
    viewModel: DepositForLoanViewModel,
    modifier: Modifier = Modifier,
    onNavigateBackToLoans: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is DepositForLoanNews.NotEnoughMoneyOnAccount ->
                    snackbarHostState.showSnackbar("На счету недостаточно средств")
                is DepositForLoanNews.ValidationError ->
                    snackbarHostState.showSnackbar("Некорректная сумма")
                is DepositForLoanNews.ExceptionDuringRequest ->
                    snackbarHostState.showSnackbar("Произошла непредвиденная ошибка")
            }
        }
    }
    DepositForLoanScreenInternal(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBackClicked = onNavigateBackToLoans,
        onStartTransaction = {
            viewModel.reduceLoanPayments(
                accountId = accountId,
                creditId = creditId,
                accountMoneyAmount = accountMoneyAmount,
            )
        },
        onError = viewModel::sendErrorNews,
        onMoneyAmountChanged = viewModel::onUiStateChanged,
        accountMoneyAmount = accountMoneyAmount,
    )
}

@Composable
private fun DepositForLoanScreenInternal(
    modifier: Modifier,
    state: DepositForLoanState,
    snackbarHostState: SnackbarHostState,
    onNavigateBackClicked: () -> Unit,
    onMoneyAmountChanged: (String) -> Unit = {},
    onStartTransaction: () -> Unit,
    accountMoneyAmount: String,
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
            accountMoneyAmount = accountMoneyAmount,
        )
    }
}

@Composable
private fun DepositContentScreen(
    modifier: Modifier = Modifier,
    state: DepositForLoanState,
    onMoneyAmountChanged: (String) -> Unit = {},
    onNavigateBackClicked: () -> Unit,
    accountMoneyAmount: String,
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
            accountMoneyAmount = accountMoneyAmount,
        )
    }
}

@Composable
private fun DepositContent(
    modifier: Modifier,
    state: DepositForLoanState,
    accountMoneyAmount: String,
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
            placeHolderText = "0 ₽",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textFieldValue = state.moneyAmount,
            onValueChange = { onMoneyAmountChanged(it) }
        )
        Text(
            text = "Сумма на вашем счете: $accountMoneyAmount ₽",
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            style = TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                color = Colors.SpanishGrey
            )
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
    state: DepositForLoanState,
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
                text = "Транзакция принята в обработку, пожалуйста ожидайте",
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
    Column(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Colors.BlueDepression,
            modifier = Modifier.size(64.dp)
        )
    }
}