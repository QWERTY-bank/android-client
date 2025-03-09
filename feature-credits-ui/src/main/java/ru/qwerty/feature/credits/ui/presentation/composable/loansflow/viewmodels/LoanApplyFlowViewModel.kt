package ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author y.yangel
 */
@HiltViewModel
@Stable
internal class LoanApplyFlowViewModel @Inject constructor() : ViewModel() {

    val state: MutableStateFlow<LoanApplyFlowState> = MutableStateFlow(LoanApplyFlowState())
    val news: MutableSharedFlow<LoanApplyFlowNews> = MutableSharedFlow(replay = 1)

    fun onAmountChanged(amount: String) {
        state.update { state -> state.copy(amount = amount) }
    }

    fun onPeriodChanged(period: String) {
        state.update { state -> state.copy(period = period) }
    }

    fun applyMaxPeriod(maxPeriod: Int, minPeriod: Int) {
        runCatching {

            val period = state.value.period.toIntOrNull() ?: run {
                news.tryEmit(LoanApplyFlowNews.PeriodValidationError)
                return@runCatching
            }

            if (period < minPeriod || period > maxPeriod) {
                news.tryEmit(LoanApplyFlowNews.PeriodValidationError)
                return@runCatching
            }

            val amountValue = state.value.amount.toIntOrNull() ?: run {
                news.tryEmit(LoanApplyFlowNews.AmountValidationError)
                return@runCatching
            }

            if (amountValue < 1000) {
                news.tryEmit(LoanApplyFlowNews.AmountValidationError)
                return@runCatching
            }

            news.tryEmit(
                LoanApplyFlowNews.NavigateToChooseAccount(
                    amount = amountValue.toString(),
                    daysAmount = period.toString()
                )
            )
        }.onFailure {
            news.tryEmit(LoanApplyFlowNews.Error)
        }
    }
}

@Immutable
internal data class LoanApplyFlowState(
    val amount: String = "",
    val period: String = "",
)

@Immutable
internal sealed interface LoanApplyFlowNews {

    data class NavigateToChooseAccount(
        val amount: String,
        val daysAmount: String,
    ) : LoanApplyFlowNews

    data object PeriodValidationError : LoanApplyFlowNews
    data object AmountValidationError : LoanApplyFlowNews
    data object Error : LoanApplyFlowNews
}