package com.example.feature.account.ui.presentation.ui.accountlist

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import com.example.feature.account.ui.domain.usecases.SendResultAfterCloseAccountFacade
import com.example.feature.account.ui.domain.usecases.SendResultForAccountListFacade
import com.example.feature.account.ui.presentation.ui.model.AccountUi
import com.example.feature.account.ui.presentation.ui.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature.account.api.CreateAccountUseCase
import ru.qwerty.feature.account.api.GetUserAccountUseCase
import ru.qwerty.feature_account_auth_api.repository.UserRepository
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class AccountListViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val sendResultAfterCloseAccountFacade: SendResultAfterCloseAccountFacade,
    private val sendResultForAccountListFacade: SendResultForAccountListFacade,
    private val userRepository: UserRepository,
) : MvvmViewModel<AccountListState>(AccountListState()) {

    val state: StateFlow<AccountListState>
        get() = internalState.asStateFlow()

    init {
        loadAccounts()
        obtainResult()
        obtainTransaction()
    }

    private fun obtainResult() {
        viewModelScope.launch(Dispatchers.Default) {
            sendResultAfterCloseAccountFacade.obtainResult().collectLatest {
                loadAccounts()
            }
        }
    }

    private fun obtainTransaction() {
        viewModelScope.launch(Dispatchers.Default) {
            sendResultForAccountListFacade.obtainTransactionEvent().collectLatest {
                loadAccounts()
            }
        }
    }

    fun loadAccounts() {
        viewModelScope.launch {
            runCatchingNonCancellation {
                internalState.setLoadingState()
                val accounts = getUserAccountUseCase()
                val hiddenAccount = userRepository.getHiddenAccounts()
                internalState.update { state ->
                    state.copy(
                        accounts = accounts.map { it.toUi(hiddenAccount) }.toImmutableList(),
                        isLoading = false,
                    )
                }
            }.onSuccess {
                internalState.setLoadingState(isLoading = false)
            }.onFailure { error ->
                Log.d("AccountListViewModel", "error", error)
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
    }

    fun createAccounts(title: String, amountType: String) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                if (title.isValidForTitle()) {
                    internalState.setLoadingState()
                    createAccountUseCase(
                        title = title.removeAllSpaces(),
                        amountType = amountType.getAmountType()
                    )
                    internalState.setLoadingState(isLoading = false)
                }
            }.onSuccess {
                loadAccounts()
            }.onFailure { error ->
                Log.d("AccountListView", error.message.toString())
                internalState.update { state ->
                    state.copy(isError = true)
                }
            }
        }
    }

    private fun String.getAmountType(): String {
        return when (this) {
            "₽" -> "Rub"
            "$" -> "Usd"
            else -> "Eur"
        }
    }

    private fun MutableStateFlow<AccountListState>.setLoadingState(isLoading: Boolean = true) {
        this.update { state -> state.copy(isLoading = isLoading, isError = false) }
    }
}

private fun String.removeAllSpaces(): String {
    val withOutSpaces = this.replace("\\s".toRegex(), "")
    return withOutSpaces
}

private fun String.isValidForTitle(): Boolean {
    return this.isNotBlank()
}

@Immutable
internal data class AccountListState(
    val accounts: ImmutableList<AccountUi> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : MvvmState


