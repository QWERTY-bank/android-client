package com.example.feature.account.ui.presentation.ui.accountlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun AccountListScreen(
    viewModel: AccountListViewModel,
    onNavigateToAccountDetails: (Int, String, Boolean, String, Color, String, Boolean) -> Unit,
    onTakeMoneyButtonTap: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AccountListScreenInternal(
        state = state,
        onCreateAccountTap = viewModel::createAccounts,
        onNavigateToAccountDetails = onNavigateToAccountDetails,
        onRetryButtonClicked = viewModel::loadAccounts,
        onTakeMoneyButtonTap = onTakeMoneyButtonTap,
    )
}

@Composable
private fun AccountListScreenInternal(
    modifier: Modifier = Modifier,
    state: AccountListState,
    onCreateAccountTap: (String, String) -> Unit,
    onNavigateToAccountDetails: (Int, String, Boolean, String, Color, String, Boolean) -> Unit,
    onRetryButtonClicked: () -> Unit,
    onTakeMoneyButtonTap: () -> Unit,
) {
    when {
        state.isError -> AccountListErrorScreen(onRetryButtonClicked = onRetryButtonClicked)
        state.isLoading -> AccountListSkeletonScreen()
        state.accounts.isNotEmpty() -> AccountListContentScreen(
            modifier = modifier,
            state = state,
            onCreateAccountTap = onCreateAccountTap,
            onNavigateToAccountDetails = onNavigateToAccountDetails,
            onTakeMoneyButtonTap = onTakeMoneyButtonTap,
        )

        state.accounts.isEmpty() -> AccountListEmptyScreen(
            modifier = modifier,
            onCreateAccountTap = onCreateAccountTap,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AccountListErrorScreen(
    modifier: Modifier = Modifier,
    onRetryButtonClicked: () -> Unit = {},
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
            onClick = onRetryButtonClicked,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AccountListSkeletonScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(128.dp),
            color = Colors.BlueDepression,
            strokeWidth = 4.dp,
        )
    }
}

