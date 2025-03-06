package com.example.feature.account.ui.presentation.ui.accountdetails

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import com.example.feature.account.ui.domain.usecases.SendResultAfterCloseAccountFacade
import com.example.feature.account.ui.domain.usecases.SendResultAfterTransactionFacade
import com.example.feature.account.ui.presentation.ui.model.AccountTransactionsUi
import com.example.feature.account.ui.presentation.ui.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature.account.api.DeleteAccountUseCase
import ru.qwerty.feature.account.api.GetAccountBalanceUseCase
import ru.qwerty.feature.account.api.GetTransactionsByAccountIdUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class AccountDetailsViewModel @Inject constructor(
    private val getTransactionsByAccountIdUseCase: GetTransactionsByAccountIdUseCase,
    private val sendResultAfterTransactionFacade: SendResultAfterTransactionFacade,
    private val sendResultAfterCloseAccountFacade: SendResultAfterCloseAccountFacade,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getAccountBalanceUseCase: GetAccountBalanceUseCase,
) : MvvmViewModel<AccountDetailsState>(AccountDetailsState()) {

    val state: StateFlow<AccountDetailsState>
        get() = internalState.asStateFlow()

    val news: MutableSharedFlow<AccountDetailsNews> = MutableSharedFlow(replay = 1)

    init {
        viewModelScope.launch {
            sendResultAfterTransactionFacade.obtainTransactionEvent().collectLatest { id ->
                loadAccountTransactions(id.toInt())
            }
        }
    }

    fun loadAccountTransactions(id: Int) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.update { it.copy(isLoading = true) }
                val accountBalance = getAccountBalanceUseCase(accountId = id.toString())
                val accountTransactions = getTransactionsByAccountIdUseCase(id = id.toString())
                internalState.update { state ->
                    state.copy(
                        transactionsUi = accountTransactions.toUi(),
                        accountBalance = accountBalance.toString(),
                    )
                }
            }.onSuccess {
                internalState.update { it.copy(isLoading = false) }
            }.onFailure {
                internalState.update { it.copy(error = true) }
            }
        }
    }

    fun closeAccountById(accountId: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.update { it.copy(isLoading = true) }
                deleteAccountUseCase(id = accountId)
            }.onSuccess {
                internalState.update { it.copy(isLoading = false) }
                sendResultAfterCloseAccountFacade.sendResultCloseAccount()
                news.emit(AccountDetailsNews.NavigateToAccountListAndRefresh)
            }.onFailure {
                internalState.update { it.copy(error = true) }
            }
        }
    }
}

@Immutable
internal data class AccountDetailsState(
    val isLoading: Boolean = false,
    val transactionsUi: AccountTransactionsUi = AccountTransactionsUi(),
    val error: Boolean = false,
    val accountBalance: String? = null,
) : MvvmState

@Immutable
internal sealed interface AccountDetailsNews {
    data object NavigateToAccountListAndRefresh : AccountDetailsNews
}