package ru.qwerty.feature.credits.ui.presentation.composable.loansflow

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.qwerty.common_uikit.component.BankCardUi
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.Colors.BlueDepression
import ru.qwerty.common_uikit.component.theme.Colors.SpanishGrey
import ru.qwerty.common_uikit.component.theme.Colors.SunshineSurprise
import ru.qwerty.common_uikit.component.theme.TextStyle
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.AccountUi
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.ChooseAccountState
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.ChooseAccountViewModel
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListErrorScreen
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListLoadingScreen

/**
 * @author y.yangel
 */
@Composable
internal fun ChooseAccountScreen(
    period: String,
    tariffId: String,
    amount: String,
    modifier: Modifier = Modifier,
    viewModel: ChooseAccountViewModel,
    onNavigateBackToLoans: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initNavigationConfig(
            tariffId = tariffId,
            period = period,
            amount = amount,
        )
    }

    when {
        state.transactionSuccess -> SuccessLoanScreen(onNavigateBackToLoans = onNavigateBackToLoans)

        state.isError -> TariffListErrorScreen(
            modifier = modifier,
            onRetryClick = viewModel::loadAccounts
        )

        state.isLoading -> TariffListLoadingScreen(modifier = modifier)

        state.accounts.isNotEmpty() -> AccountListContentScreen(
            modifier = modifier,
            state = state,
            onAccountTap = viewModel::applyLoans
        )

        state.accounts.isEmpty() -> AccountListEmptyScreen()
    }

}

@Composable
internal fun AccountListContentScreen(
    state: ChooseAccountState,
    modifier: Modifier = Modifier,
    onAccountTap: (Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        item { SectionTitle(text = "Выберите счет, на который перечислим размер кредита") }
        DisplayAccounts(
            accounts = state.accounts.filter { !it.isClosed },
            onAccountTap = onAccountTap,
        )
    }
}

private fun LazyListScope.DisplayAccounts(
    accounts: List<AccountUi>,
    onAccountTap: (Int) -> Unit,
) {
    itemsIndexed(accounts) { index, account ->
        val baseColor = if (index % 2 == 0) SunshineSurprise else BlueDepression
        BankCardUi(
            modifier = Modifier.clickable { onAccountTap(account.id) },
            balance = account.moneyAmount,
            cardTitle = account.title,
            baseColor = baseColor,
            accountCurrencyType = account.accountCurrencyType,
            cardNumber = account.id,
        )
    }
}

@Composable
private fun SectionTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = androidx.compose.ui.text.TextStyle(
            fontFamily = LatoFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Colors.JetBlack,
            letterSpacing = 1.sp,
        )
    )
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
            style = TextStyle.MediumFontStyle,
            textAlign = TextAlign.Center,
            color = SpanishGrey,
            fontSize = 16.sp,
        )
    }
}
