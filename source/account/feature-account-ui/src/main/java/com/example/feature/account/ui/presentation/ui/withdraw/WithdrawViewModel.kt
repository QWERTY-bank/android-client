package com.example.feature.account.ui.presentation.ui.withdraw

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import com.example.feature.account.ui.domain.usecases.SendResultAfterTransactionFacade
import com.example.feature.account.ui.domain.usecases.SendResultForAccountListFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature.account.api.WithdrawUseCase
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
@HiltViewModel
@OptIn(ExperimentalUuidApi::class)
internal class WithdrawViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val sendResultAfterTransactionFacade: SendResultAfterTransactionFacade,
    private val sendResultForAccountListFacade: SendResultForAccountListFacade,
) : MvvmViewModel<WithdrawScreenState>(WithdrawScreenState()) {

    val state: StateFlow<WithdrawScreenState>
        get() = internalState.asStateFlow()

    private val _news: MutableSharedFlow<WithdrawScreenNews> = MutableSharedFlow(replay = 1)
    val news: SharedFlow<WithdrawScreenNews>
        get() = _news.asSharedFlow()

    private val transactionKey by lazy { Uuid.random().toString() }

    fun withdrawFromAccount(currentBalance: Double, withdrawAmount: Double, accountId: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                if (currentBalance - withdrawAmount < 0) {
                    _news.emit(WithdrawScreenNews.ValidationError)
                } else {
                    internalState.update { it.copy(isLoading = true) }
                    withdrawUseCase(
                        id = accountId,
                        withdrawAmount = withdrawAmount.toString(),
                        key = transactionKey
                    )
                    setSuccessState()
                }
            }.onFailure {
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
            _news.emit(WithdrawScreenNews.ExceptionDuringRequest)
        }
    }

    fun onBackButtonPressed(accountId: String) {
        viewModelScope.launch {
            sendResultAfterTransactionFacade.sendResultAfterTransaction(id = accountId)
            sendResultForAccountListFacade.sendResultAfterTransaction()
            _news.emit(WithdrawScreenNews.NavigateToAccountDetailsAndUpdate)
        }
    }

    private fun setSuccessState() {
        internalState.update { it.copy(successWithdraw = true) }
        internalState.update { it.copy(isLoading = false) }
    }
}

@Immutable
internal data class WithdrawScreenState(
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val successWithdraw: Boolean = false,
    var moneyAmount: String = "",
) : MvvmState

@Immutable
internal sealed interface WithdrawScreenNews {
    data object NavigateToAccountDetailsAndUpdate : WithdrawScreenNews
    data object ValidationError : WithdrawScreenNews
    data object ExceptionDuringRequest : WithdrawScreenNews
}