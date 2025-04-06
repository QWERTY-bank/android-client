package com.example.feature.account.ui.presentation.ui.accountdetails

import android.util.Log
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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
import ru.qwerty.feature.account.api.ObtainTransactionsUseCase
import ru.qwerty.feature_account_auth_api.repository.UserRepository
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class AccountDetailsViewModel @Inject constructor(
    private val getTransactionsByAccountIdUseCase: GetTransactionsByAccountIdUseCase,
    private val obtainTransactionsUseCase: ObtainTransactionsUseCase,
    private val sendResultAfterTransactionFacade: SendResultAfterTransactionFacade,
    private val sendResultAfterCloseAccountFacade: SendResultAfterCloseAccountFacade,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    private val userRepository: UserRepository,
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
        viewModelScope.launch {
            obtainAccountTransaction()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCleared() {
        GlobalScope.launch(Dispatchers.IO) {
            obtainTransactionsUseCase.closeConnection()
        }
        super.onCleared()
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
                obtainTransactionsUseCase.setupConnection(id.toString())
            }.onFailure { error ->
                Log.d("AccountDetailsViewModel", error.message.toString())
                internalState.update { it.copy(error = true) }
            }
        }
    }

    private suspend fun obtainAccountTransaction() {
        obtainTransactionsUseCase().collectLatest {
            internalState.update { state ->
                val transactionList = state.transactionsUi.transactions.toMutableList()
                transactionList.add(0, it.second.toUi())
                state.copy(
                    accountBalance = it.first.moneyAmount,
                    transactionsUi = state.transactionsUi.copy(
                        transactions = transactionList.toImmutableList()

                    )
                )
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

    fun setHiddenAccount(isHidden: Boolean, accountId: Int) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                userRepository.setHiddenAccount(isHidden, accountId)
            }.onSuccess {
                Log.d("AccountDetailsViewModel", "hidden success set")
            }.onFailure { error ->
                Log.d("AccountDetailsViewModel", error.message.toString())
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