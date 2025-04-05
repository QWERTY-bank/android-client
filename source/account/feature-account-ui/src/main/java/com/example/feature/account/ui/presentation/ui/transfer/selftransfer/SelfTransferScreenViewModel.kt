package com.example.feature.account.ui.presentation.ui.transfer.selftransfer

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature.account.api.GetUserAccountUseCase
import ru.qwerty.feature.account.api.domain.Account
import javax.inject.Inject

@Stable
@HiltViewModel
internal class SelfTransferScreenViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
) : MvvmViewModel<ChooseAccountState>(ChooseAccountState()) {

    val state: StateFlow<ChooseAccountState>
        get() = internalState.asStateFlow()

    fun loadAccounts(fromAccountId: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.setLoadingState()
                val accounts = getUserAccountUseCase()
                internalState.update { state ->
                    state.copy(
                        accounts = accounts
                            .filter { !it.isClosed }
                            .filter { it.id != fromAccountId.toInt() }
                            .map { it.toUi() }
                            .toImmutableList(),
                        isLoading = false,
                    )
                }
            }.onSuccess {
                internalState.setLoadingState(isLoading = false)
            }.onFailure {
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
    }

    fun retry(fromAccountId: String) {
        loadAccounts(fromAccountId)
    }


    private fun MutableStateFlow<ChooseAccountState>.setLoadingState(isLoading: Boolean = true) {
        this.update { state -> state.copy(isLoading = isLoading, isError = false) }
    }
}

@Immutable
internal data class ChooseAccountState(
    val accounts: ImmutableList<AccountUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : MvvmState

@Immutable
internal data class AccountUi(
    val id: Int,
    val moneyAmount: String,
    val isClosed: Boolean,
    val title: String,
    val userId: String,
    val accountCurrencyType: String,
)

internal fun Account.toUi(): AccountUi {
    return AccountUi(
        id = this.id,
        moneyAmount = this.moneyAmount.value.toString(),
        isClosed = this.isClosed,
        title = this.title,
        userId = this.userId,
        accountCurrencyType = moneyAmount.code ?: "Rub",
    )
}


