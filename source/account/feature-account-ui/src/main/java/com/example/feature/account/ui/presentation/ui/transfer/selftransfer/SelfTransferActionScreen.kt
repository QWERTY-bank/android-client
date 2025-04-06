package com.example.feature.account.ui.presentation.ui.transfer.selftransfer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature.account.ui.R
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors

@Composable
internal fun SelfTransferActionScreen(
    fromAccountId: String,
    toAccountId: String,
    moneyAmountOnAccount: String,
    accountCurrencyType: String,
    viewModel: SelfTransferActionViewModel,
    onSuccessButtonClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.initViewModelUiConfig(
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        moneyOnAccount = moneyAmountOnAccount,
        code = accountCurrencyType,
    )

    when {
        state.successWithdraw -> SuccessSelfTransferActionScreen(onSuccessButtonClicked)
        state.isLoading -> DepositLoadingScreen()
        else -> SelfTransferContent(
            state = state,
            onMoneyAmountChanged = viewModel::onUiStateChanged,
            onStartTransaction = viewModel::depositOnAccount,
            onNavigateToHome = onSuccessButtonClicked,
            onRetry = viewModel::onRetry,
        )
    }
}

@Composable
private fun SelfTransferContent(
    modifier: Modifier = Modifier,
    state: DepositScreenState,
    onNavigateToHome: () -> Unit,
    onRetry: () -> Unit,
    onMoneyAmountChanged: (String) -> Unit,
    onStartTransaction: () -> Unit,
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
            state.error -> SelfTransferErrorScreen(onRetryClick = onRetry)
            else -> SelfTransferButton(
                onStartTransaction = onStartTransaction,
                state = state,
            )
        }
    }
}

@Composable
private fun DepositLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Colors.BlueDepression,
            modifier = Modifier.size(64.dp),
            strokeWidth = 2.dp
        )
    }
}

@Composable
internal fun SelfTransferErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Произошла ошибка во время выполнения вашего запроса",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                color = Colors.PeppyPeacock
            )
        )
        AccentButton(
            text = "Повторить",
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetryClick,
        )
    }
}

@Composable
private fun ColumnScope.SelfTransferButton(
    onStartTransaction: () -> Unit,
    state: DepositScreenState
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally()
                + expandHorizontally(expandFrom = Alignment.End)
                + fadeIn()
    ) {
        AccentButton(
            text = "Пополнить",
            modifier = Modifier.padding(top = 24.dp),
            onClick = onStartTransaction,
            isEnabled = state.moneyAmount.isNotBlank(),
        )
    }
}

@Composable
private fun SuccessSelfTransferActionScreen(
    onNavigateBackClicked: () -> Unit,
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