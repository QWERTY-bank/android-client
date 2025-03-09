package ru.qwerty.feature.credits.ui.presentation.composable.loansreduce

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ru.qwerty.common_uikit.component.theme.Colors.SunshineSurprise
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.AccountUi
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListErrorScreen
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.TariffListLoadingScreen

/**
 * @author y.yangel
 */
@Composable
internal fun LoansReduceChooseAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: LoansReduceChooseAccountViewModel,
    creditId: String,
    onAccountTap: (String, Int, String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when {
        state.isError -> TariffListErrorScreen(
            modifier = modifier,
            onRetryClick = viewModel::loadAccounts
        )

        state.isLoading -> TariffListLoadingScreen(modifier = modifier)

        state.accounts.isNotEmpty() -> LoansReduceChooseAccountScreenContent(
            modifier = modifier,
            state = state,
            onAccountTap = onAccountTap,
            creditId = creditId,
        )
    }

}

@Composable
private fun LoansReduceChooseAccountScreenContent(
    state: LoansReduceChooseAccountState,
    modifier: Modifier = Modifier,
    creditId: String,
    onAccountTap: (String, Int, String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        item { SectionTitle(text = "Выберите счет, с которого хотите оплатить") }
        DisplayAccounts(
            accounts = state.accounts.filter { !it.isClosed },
            creditId = creditId,
            onAccountTap = onAccountTap,
        )
    }
}

private fun LazyListScope.DisplayAccounts(
    accounts: List<AccountUi>,
    creditId: String,
    onAccountTap: (String, Int, String) -> Unit,
) {
    itemsIndexed(accounts) { index, account ->
        val baseColor = if (index % 2 == 0) SunshineSurprise else BlueDepression
        BankCardUi(
            modifier = Modifier.clickable { onAccountTap(account.moneyAmount, account.id, creditId) },
            balance = account.moneyAmount,
            cardTitle = account.title,
            baseColor = baseColor,
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
