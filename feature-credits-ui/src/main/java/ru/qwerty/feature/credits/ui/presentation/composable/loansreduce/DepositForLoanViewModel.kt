package ru.qwerty.feature.credits.ui.presentation.composable.loansreduce

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.feature.credits.api.usecases.ReduceLoanPaymentsUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class DepositForLoanViewModel @Inject constructor(
    private val reduceLoanPaymentsUseCase: ReduceLoanPaymentsUseCase,
) : MvvmViewModel<DepositForLoanState>(DepositForLoanState()) {

    val state: StateFlow<DepositForLoanState>
        get() = internalState.asStateFlow()

    private val _news: MutableSharedFlow<DepositForLoanNews> = MutableSharedFlow(replay = 1)
    val news: SharedFlow<DepositForLoanNews>
        get() = _news.asSharedFlow()


    fun reduceLoanPayments(creditId: String, accountId: Int, accountMoneyAmount: String) {
        viewModelScope.launch {
            runCatching {
                internalState.update { it.copy(isLoading = false) }
                val value = state.value.moneyAmount.toDoubleOrNull() ?: run {
                    _news.tryEmit(DepositForLoanNews.ValidationError)
                    return@runCatching
                }
                val accountMoneyAmountDouble = accountMoneyAmount.toDoubleOrNull() ?: run {
                    _news.tryEmit(DepositForLoanNews.ValidationError)
                    return@runCatching
                }
                if (accountMoneyAmountDouble < value) {
                    _news.tryEmit(DepositForLoanNews.NotEnoughMoneyOnAccount)
                    return@runCatching
                }
                reduceLoanPaymentsUseCase(
                    creditId = creditId,
                    accountId = accountId,
                    value = value,
                )
            }.onSuccess {
                setSuccessState()
            }.onFailure { error ->
                Log.d("ReduceLoanPayments", "TAG", error)
                internalState.update { it.copy(error = true) }
            }
        }
    }

    fun onUiStateChanged(moneyAmount: String) {
        internalState.update {
            it.copy(moneyAmount = moneyAmount)
        }
    }

    fun sendErrorNews() {
        viewModelScope.launch {
            _news.emit(DepositForLoanNews.ExceptionDuringRequest)
        }
    }

    private fun setSuccessState() {
        internalState.update { it.copy(successWithdraw = true) }
        internalState.update { it.copy(isLoading = false) }
    }
}

@Immutable
internal data class DepositForLoanState(
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val successWithdraw: Boolean = false,
    var moneyAmount: String = "",
) : MvvmState

@Immutable
internal sealed interface DepositForLoanNews {
    data object ValidationError : DepositForLoanNews
    data object NotEnoughMoneyOnAccount : DepositForLoanNews
    data object ExceptionDuringRequest : DepositForLoanNews
}