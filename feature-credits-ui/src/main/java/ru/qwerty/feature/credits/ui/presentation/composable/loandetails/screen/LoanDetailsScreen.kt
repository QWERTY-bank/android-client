package ru.qwerty.feature.credits.ui.presentation.composable.loandetails.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel.LoanDetailsState
import ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel.LoanDetailsViewModel
import ru.qwerty.feature.credits.ui.presentation.composable.loans.LoansListLoadingScreen

/**
 * @author y.yangel
 */
@Composable
internal fun LoanDetailsScreen(
    loanId: String,
    viewModel: LoanDetailsViewModel,
    onNavigateBackToLoanList: () -> Unit,
    onPayClick: (String) -> Unit,
) {
    BackHandler {
        onNavigateBackToLoanList()
    }
    LaunchedEffect(Unit) {
        viewModel.loadDetails(loanId)
    }
    val state by viewModel.internalState.collectAsStateWithLifecycle()
    LoanDetailsScreenInternal(
        state = state,
        onRetryClick = { viewModel.loadDetails(loanId) },
        onPayClick = onPayClick,
    )

}

@Composable
private fun LoanDetailsScreenInternal(
    modifier: Modifier = Modifier,
    state: LoanDetailsState,
    onRetryClick: () -> Unit,
    onPayClick: (String) -> Unit,
) {
    when {
        state.details != null -> LoanDetailsScreenContent(
            modifier = modifier,
            loanDetails = state.details,
            onPayClick = onPayClick,
            state = state,
        )

        state.isError -> LoanDetailsErrorScreen(modifier = modifier, onRetryClick = onRetryClick)
        state.isLoading -> LoansListLoadingScreen()
    }
}

@Composable
private fun LoanDetailsErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
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
            onClick = { onRetryClick() },
        )
    }
}
