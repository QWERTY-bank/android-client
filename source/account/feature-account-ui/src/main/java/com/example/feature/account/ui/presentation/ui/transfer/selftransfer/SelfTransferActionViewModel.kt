package com.example.feature.account.ui.presentation.ui.transfer.selftransfer

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature.account.api.repository.AccountsRepository
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
@HiltViewModel
@OptIn(ExperimentalUuidApi::class)
internal class SelfTransferActionViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository,
) : MvvmViewModel<DepositScreenState>(DepositScreenState()) {

    val state: StateFlow<DepositScreenState>
        get() = internalState.asStateFlow()

    private val transactionKey by lazy { Uuid.random().toString() }

    fun initViewModelUiConfig(
        fromAccountId: String,
        toAccountId: String,
        moneyOnAccount: String,
        code: String,
    ) {
        internalState.update { state ->
            state.copy(
                moneyAmountOnAccount = moneyOnAccount,
                toAccountId = toAccountId,
                fromAccountId = fromAccountId,
                code = code,
            )
        }
    }

    fun depositOnAccount() = with(internalState.value) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                if (moneyAmount.toDouble() <= moneyAmountOnAccount.toDouble()) {
                    Log.d("SelfTransferActionViewModel", moneyAmountOnAccount)
                    accountsRepository.transferToAccount(
                        fromAccountId = fromAccountId,
                        toAccountId = toAccountId,
                        amount = moneyAmount,
                        key = transactionKey,
                        code = code,
                    )
                }
                else {
                    throw RuntimeException()
                }
            }.onSuccess { setSuccessState() }
                .onFailure { error ->
                    Log.d("SelfTransferActionViewModel", error.message.toString())
                    internalState.update { it.copy(error = true) }
                }
        }
    }

    fun onRetry() {
        depositOnAccount()
    }

    fun onUiStateChanged(moneyAmount: String) {
        internalState.update {
            it.copy(moneyAmount = moneyAmount)
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
    val moneyAmountOnAccount: String = "",
    val fromAccountId: String = "",
    val toAccountId: String = "",
    val code: String = "",
) : MvvmState
