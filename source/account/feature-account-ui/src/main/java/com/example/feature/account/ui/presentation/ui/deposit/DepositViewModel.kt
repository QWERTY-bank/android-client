package com.example.feature.account.ui.presentation.ui.deposit

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
import ru.qwerty.feature.account.api.DepositUseCase
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
@HiltViewModel
@OptIn(ExperimentalUuidApi::class)
internal class DepositViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val sendResultAfterTransactionFacade: SendResultAfterTransactionFacade,
    private val sendResultForAccountListFacade: SendResultForAccountListFacade,
) : MvvmViewModel<DepositScreenState>(DepositScreenState()) {

    val state: StateFlow<DepositScreenState>
        get() = internalState.asStateFlow()

    private val _news: MutableSharedFlow<DepositScreenNews> = MutableSharedFlow(replay = 1)
    val news: SharedFlow<DepositScreenNews>
        get() = _news.asSharedFlow()

    private val transactionKey by lazy { Uuid.random().toString() }

    fun depositOnAccount(depositAmount: Double, accountId: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                if (depositAmount < 1 || depositAmount > 1000000) {
                    _news.emit(DepositScreenNews.ValidationError)
                } else {
                    internalState.update { it.copy(isLoading = true) }
                    depositUseCase(
                        id = accountId,
                        depositAmount = depositAmount.toString(),
                        key = transactionKey,
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
            _news.emit(DepositScreenNews.ExceptionDuringRequest)
        }
    }

    fun onBackButtonPressed(accountId: String) {
        viewModelScope.launch {
            sendResultAfterTransactionFacade.sendResultAfterTransaction(id = accountId)
            sendResultForAccountListFacade.sendResultAfterTransaction()
            _news.emit(DepositScreenNews.NavigateToAccountDetailsAndUpdate)
        }
    }

    private fun setSuccessState() {
        internalState.update { it.copy(successWithdraw = true) }
        internalState.update { it.copy(isLoading = false) }
    }
}

@Immutable
internal data class DepositScreenState(
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val successWithdraw: Boolean = false,
    var moneyAmount: String = "",
) : MvvmState

@Immutable
internal sealed interface DepositScreenNews {
    data object NavigateToAccountDetailsAndUpdate : DepositScreenNews
    data object ValidationError : DepositScreenNews
    data object ExceptionDuringRequest : DepositScreenNews
}