package ru.qwerty.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.coroutines.runCatchingNonCancellation
import ru.qwerty.feature_account_auth_api.IsUserAlreadyLoginUseCase
import ru.qwerty.feature_account_auth_api.accounts.LoginUserByPhoneUseCase
import ru.qwerty.ui.domain.ValidatePasswordInputUseCase
import ru.qwerty.ui.domain.ValidatePhoneInputUseCase
import ru.qwerty.ui.domain.ValidationResult
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class AccountViewModel @Inject constructor(
    private val loginUserByPhoneUseCase: LoginUserByPhoneUseCase,
    private val validatePhoneInputUseCase: ValidatePhoneInputUseCase,
    private val validatePasswordInputUseCase: ValidatePasswordInputUseCase,
) : MvvmViewModel<AccountState>(AccountState()) {

    val state: StateFlow<AccountState>
        get() = internalState.asStateFlow()

    private val _news = MutableSharedFlow<AccountNews>(replay = 1)
    val news = _news.asSharedFlow()

    fun loginUserByPhone() {
        viewModelScope.launch {
            internalState.setLoadingState()
            runCatchingNonCancellation {
                with(internalState.value) {
                    if (accountUiState.isValidationPassed()) {
                        loginUserByPhoneUseCase(
                            phoneNumber = accountUiState.phone,
                            password = accountUiState.password,
                        )
                    } else {
                        _news.tryEmit(AccountNews.AccountNewsSnackbar)
                    }
                }
            }.onSuccess {
                _news.tryEmit(AccountNews.NavigateToAccountListScreenNews)
            }.onFailure { error ->
                internalState.setErrorState()
                Log.d(
                    /* tag = */ "AccountViewModel",
                    /* msg = */"exception during login",
                    /* tr = */ error,
                )
            }
        }
    }

    fun onPhoneInput(phoneInput: String) {
        internalState.update { state ->
            state.copy(
                accountUiState = state.accountUiState.copy(
                    phone = phoneInput,
                    isPhoneValid = validatePhoneInputUseCase(phoneInput = phoneInput)
                )
            )
        }
    }

    fun onPasswordInput(passwordInput: String) {
        internalState.update { state ->
            state.copy(
                accountUiState = state.accountUiState.copy(
                    password = passwordInput,
                    isPasswordValid = validatePasswordInputUseCase(passwordInput = passwordInput)
                )
            )
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

    private fun MutableStateFlow<AccountState>.setLoadingState() {
        this.update { state ->
            state.copy(
                accountRemoteState = state.accountRemoteState.copy(
                    isLoading = true,
                    error = false
                )
            )
        }
    }
}

internal sealed interface AccountNews {
    data object AccountNewsSnackbar : AccountNews
    data object NavigateToAccountListScreenNews : AccountNews
}

internal data class AccountState(
    val accountRemoteState: AccountRemoteState = AccountRemoteState(),
    val accountUiState: AccountUiState = AccountUiState(),
) : MvvmState

internal data class AccountRemoteState(
    val isLoading: Boolean = false,
    val error: Boolean = false,
)

internal data class AccountUiState(
    val phone: String = "78005553535",
    val password: String = "3101911Gg",
    val isPhoneValid: ValidationResult = ValidationResult(isValid = true),
    val isPasswordValid: ValidationResult = ValidationResult(isValid = true),
)

private fun AccountUiState.isValidationPassed(): Boolean {
    return when {
        isPhoneValid.isValid == true && isPasswordValid.isValid == true -> true
        else -> false
    }
}
