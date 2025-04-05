@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.feature.account.ui.presentation.ui.accountlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.account.ui.R
import com.example.feature.account.ui.presentation.ui.model.AccountUi
import ru.qwerty.common_uikit.component.BankCardUi
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.Colors.BlueDepression
import ru.qwerty.common_uikit.component.theme.Colors.SunshineSurprise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountListContentScreen(
    state: AccountListState,
    modifier: Modifier = Modifier,
    onCreateAccountTap: (String, String) -> Unit,
    onNavigateToAccountDetails: (Int, String, Boolean, String, Color, String) -> Unit,
    onTakeMoneyButtonTap: () -> Unit,
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        item { SectionTitle(text = "Ваши счета", color = Colors.JetBlack) }

        DisplayAccounts(
            accounts = state.accounts.filter { !it.isClosed },
            onNavigateToAccountDetails = onNavigateToAccountDetails
        )
        item { AddAccountButton(showBottomSheet) }
        item {
            AccentButton(
                text = "Кредиты",
                modifier = Modifier.padding(top = 8.dp),
                onClick = onTakeMoneyButtonTap,
            )
        }

        if (state.accounts.any { it.isClosed }) {
            item {
                SectionTitle(
                    text = "Закрытые счета",
                    modifier = Modifier.clickable { expanded = !expanded },
                    color = Colors.PeppyPeacock
                )
            }
            DisplayClosedAccounts(
                accounts = state.accounts,
                onNavigateToAccountDetails = onNavigateToAccountDetails
            )
        }
    }

    AddAccountBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onCreateAccountTap = onCreateAccountTap,
    )
}

@Composable
private fun SectionTitle(modifier: Modifier = Modifier, text: String, color: Color) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(
            fontFamily = LatoFont,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            letterSpacing = 1.sp,
            color = color
        )
    )
}

private fun LazyListScope.DisplayAccounts(
    accounts: List<AccountUi>,
    onNavigateToAccountDetails: (Int, String, Boolean, String, Color, String) -> Unit
) {
    itemsIndexed(accounts) { index, account ->
        val baseColor = if (index % 2 == 0) SunshineSurprise else BlueDepression
        BankCardUi(
            modifier = Modifier.clickable {
                onNavigateToAccountDetails(
                    account.id,
                    account.title,
                    account.isClosed,
                    account.moneyAmount,
                    baseColor,
                    account.accountCurrencyType,
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

private fun LazyListScope.DisplayClosedAccounts(
    accounts: List<AccountUi>,
    onNavigateToAccountDetails: (Int, String, Boolean, String, Color, String) -> Unit
) {
    itemsIndexed(accounts.filter { it.isClosed }) { index, account ->
        val baseColor = if (index % 2 == 0) SunshineSurprise else BlueDepression
        BankCardUi(
            modifier = Modifier.clickable {
                onNavigateToAccountDetails(
                    account.id,
                    account.title,
                    account.isClosed,
                    account.moneyAmount,
                    baseColor,
                    account.accountCurrencyType,
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


@Composable
private fun AddAccountButton(showBottomSheet: MutableState<Boolean>) {
    AccentButton(
        modifier = Modifier.padding(top = 24.dp),
        text = stringResource(R.string.add_account),
        onClick = { showBottomSheet.value = true }
    )
}