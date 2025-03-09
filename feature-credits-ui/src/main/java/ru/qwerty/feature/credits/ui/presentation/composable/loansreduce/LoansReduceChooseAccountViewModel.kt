package ru.qwerty.feature.credits.ui.presentation.composable.loansreduce

import android.util.Log
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
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.AccountUi
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.toUi
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class LoansReduceChooseAccountViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
) : MvvmViewModel<LoansReduceChooseAccountState>(LoansReduceChooseAccountState()) {

    val state: StateFlow<LoansReduceChooseAccountState>
        get() = internalState.asStateFlow()

    init {
        loadAccounts()
    }

    fun loadAccounts() {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.setLoadingState()
                val accounts = getUserAccountUseCase()
                internalState.update { state ->
                    state.copy(
                        accounts = accounts
                            .filter { !it.isClosed }
                            .map { it.toUi() }
                            .toImmutableList(),
                        isLoading = false,
                    )
                }
            }.onSuccess {
                internalState.setLoadingState(isLoading = false)
            }.onFailure { error ->
                Log.d("LoansReduceChooseAccountViewModel", "error", error)
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
    }

    private fun MutableStateFlow<LoansReduceChooseAccountState>.setLoadingState(isLoading: Boolean = true) {
        this.update { state -> state.copy(isLoading = isLoading, isError = false) }
    }
}

@Immutable
internal data class LoansReduceChooseAccountState(
    val accounts: ImmutableList<AccountUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val transactionSuccess: Boolean = false,
    val amount: String = "",
    val trafficId: String = "",
    val period: String = "",
) : MvvmState