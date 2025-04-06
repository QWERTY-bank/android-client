package ru.qwerty.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature_account_auth_api.accounts.LoginUserUseCase
import ru.qwerty.ui.domain.ValidatePasswordInputUseCase
import ru.qwerty.ui.domain.ValidatePhoneInputUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class AccountViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : MvvmViewModel<AccountState>(AccountState()) {

    val state: StateFlow<AccountState>
        get() = internalState.asStateFlow()

    private val _news = MutableSharedFlow<AccountNews>(replay = 1)
    val news = _news.asSharedFlow()


    fun loginUserByCode(code: String) {
        viewModelScope.launch {
            internalState.setLoadingState()
            runCatchingNonCancellation {
                loginUserUseCase.invoke(code)
            }.onSuccess {
                _news.tryEmit(AccountNews.NavigateToAccountListScreenNews)
            }.onFailure { error ->
                Log.d("AccountViewModel", "error", error)
                internalState.setErrorState()
            }
        }
    }

    private fun MutableStateFlow<AccountState>.setErrorState() {
        this.update { state ->
            state.copy(
                accountRemoteState = state.accountRemoteState.copy(
                    isLoading = false,
                    error = true
                )
            )
        }
    }

    private fun MutableStateFlow<AccountState>.setLoadingState(isLoading: Boolean = true) {
        this.update { state ->
            state.copy(
                accountRemoteState = state.accountRemoteState.copy(
                    isLoading = isLoading,
                    error = false
                )
            )
        }
    }
}

internal sealed interface AccountNews {
    data object NavigateToAccountListScreenNews : AccountNews
}

@Immutable
internal data class AccountState(
    val accountRemoteState: AccountRemoteState = AccountRemoteState(),
) : MvvmState

@Immutable
internal data class AccountRemoteState(
    val isLoading: Boolean = false,
    val error: Boolean = false,
)
