package ru.qwerty.feature.credits.ui.presentation.composable.tariff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import ru.qwerty.feature.credits.api.domain.CreditType
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffListState
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffListViewModel
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffUi
import ru.qwerty.feature.credits.ui.presentation.navigation.navigateToChooseAccountLambda

/**
 * @author y.yangel
 */
@Composable
internal fun TariffListScreen(
    modifier: Modifier = Modifier,
    viewModel: TariffListViewModel,
    onTariffTap: (String, String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TariffListScreenInternal(
        modifier = modifier,
        state = state,
        onTariffTap = onTariffTap,
        onRetryButton = viewModel::loadTariffs
    )
}

@Composable
private fun TariffListScreenInternal(
    modifier: Modifier = Modifier,
    state: TariffListState,
    onRetryButton: () -> Unit,
    onTariffTap: (String, String, String) -> Unit,
) {
    when {
        state.isError -> TariffListErrorScreen(modifier = modifier, onRetryClick = onRetryButton)
        state.isLoading -> TariffListLoadingScreen(modifier = modifier)
        state.tariffList.isNotEmpty() -> TariffListContentScreen(
            modifier = modifier,
            state = state,
            onTariffTap = onTariffTap,
        )

        else -> TariffListLoadingScreen(modifier = modifier)
    }
}

@Preview
@Composable
private fun TariffListScreenPreview() {
    TariffListScreenInternal(
        state = TariffListState(
            tariffList = mockTariffUiList,
        ),
        onRetryButton = {},
        onTariffTap = navigateToChooseAccountLambda,
    )
}

private val mockTariffUiList = persistentListOf(
    TariffUi(
        id = "1",
        name = "Basic Plan",
        interestRate = 5,
        interestRateType = CreditType.Annual,
        minPeriodDays = 30,
        maxPeriodDays = 365,
    ),
    TariffUi(
        id = "2",
        name = "Premium Plan",
        interestRate = 8,
        interestRateType = CreditType.Monthly,
        minPeriodDays = 60,
        maxPeriodDays = 730,
    ),
    TariffUi(
        id = "3",
        name = "Flexi Plan",
        interestRate = 10,
        interestRateType = CreditType.Daytime,
        minPeriodDays = 90,
        maxPeriodDays = 1095,
    )
)