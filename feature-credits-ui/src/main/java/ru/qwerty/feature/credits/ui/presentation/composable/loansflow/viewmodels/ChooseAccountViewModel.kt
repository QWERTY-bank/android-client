package ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels

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
import ru.qwerty.feature.credits.api.usecases.ApplyLoansUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class ChooseAccountViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val applyLoansUseCase: ApplyLoansUseCase,
) : MvvmViewModel<ChooseAccountState>(ChooseAccountState()) {

    val state: StateFlow<ChooseAccountState>
        get() = internalState.asStateFlow()

    init {
        loadAccounts()
    }

    fun initNavigationConfig(
        tariffId: String,
        period: String,
        amount: String,
    ) {
        internalState.update { state ->
            state.copy(
                amount = amount,
                trafficId = period,
                period = tariffId,
            )
        }
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
            }.onFailure {
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
    }

    fun applyLoans(accountId: Int) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.setLoadingState()
                applyLoansUseCase(
                    accountId = accountId,
                    tariffId = internalState.value.period,
                    periodDays = internalState.value.trafficId.toInt(),
                    loanAmount = internalState.value.amount.toDouble(),
                )
            }.onSuccess {
                internalState.setLoadingState(isLoading = false)
                internalState.update { it.copy(transactionSuccess = true) }
            }.onFailure {
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
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
    val transactionSuccess: Boolean = false,
    val amount: String = "",
    val trafficId: String = "",
    val period: String = "",
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