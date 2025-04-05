package com.example.feature.account.ui.presentation.ui.transfer.selftransfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.qwerty.common_uikit.component.BankCardUi
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.Colors.BlueDepression
import ru.qwerty.common_uikit.component.theme.Colors.SpanishGrey
import ru.qwerty.common_uikit.component.theme.Colors.SunshineSurprise

@Composable
internal fun SelfTransferScreen(
    fromAccountId: String,
    moneyAmount: String,
    accountCurrencyType: String,
    onAccountTap: (String, String, String, String) -> Unit,
    viewModel: SelfTransferScreenViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts(fromAccountId)
    }

    when {
        state.isError -> SelfTransferErrorScreen(
            onRetryClick = viewModel::retry,
            fromAccountId = fromAccountId
        )

        state.isLoading -> SelfTransferLoadingScreen()

        state.accounts.isNotEmpty() -> AccountListContentScreen(
            onAccountTap = onAccountTap,
            moneyAmount = moneyAmount,
            state = state,
            fromAccountId = fromAccountId,
            accountCurrencyType = accountCurrencyType,
        )

        state.accounts.isEmpty() -> AccountListEmptyScreen()
    }
}


@Composable
internal fun AccountListContentScreen(
    state: ChooseAccountState,
    moneyAmount: String,
    fromAccountId: String,
    accountCurrencyType: String,
    onAccountTap: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        item { SectionTitle(text = "Выберите счет") }
        DisplayAccounts(
            accounts = state.accounts.filter { !it.isClosed },
            moneyAmount = moneyAmount,
            fromAccountId = fromAccountId,
            onAccountTap = onAccountTap,
            code = accountCurrencyType,
        )
    }
}

@Composable
internal fun SelfTransferLoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Colors.BlueDepression,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
private fun AccountListEmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "У вас нет ни одного активного счета, можете открыть счет на Главной",
            modifier = Modifier.fillMaxWidth(),
            style = ru.qwerty.common_uikit.component.theme.TextStyle.MediumFontStyle,
            textAlign = TextAlign.Center,
            color = SpanishGrey,
            fontSize = 16.sp,
        )
    }
}


@Composable
internal fun SelfTransferErrorScreen(
    modifier: Modifier = Modifier,
    fromAccountId: String,
    onRetryClick: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Произошла ошибка во время\nвыполнения вашего запроса",
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
            onClick = { onRetryClick(fromAccountId) },
        )
    }
}

@Composable
fun SectionTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(
            fontFamily = LatoFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Colors.JetBlack,
            letterSpacing = 1.sp,
        )
    )
}

private fun LazyListScope.DisplayAccounts(
    fromAccountId: String,
    accounts: List<AccountUi>,
    moneyAmount: String,
    code: String,
    onAccountTap: (String, String, String, String) -> Unit,
) {
    itemsIndexed(accounts) { index, account ->
        val baseColor = if (index % 2 == 0) SunshineSurprise else BlueDepression
        BankCardUi(
            modifier = Modifier.clickable {
                onAccountTap(
                    fromAccountId,
                    account.id.toString(),
                    moneyAmount,
                    code
                )
            },
            balance = account.moneyAmount,
            cardTitle = account.title,
            baseColor = baseColor,
            accountCurrencyType = account.accountCurrencyType,
            cardNumber = account.id,
        )
    }
}
