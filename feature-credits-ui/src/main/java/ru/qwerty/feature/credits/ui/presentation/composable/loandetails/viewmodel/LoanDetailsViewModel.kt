package ru.qwerty.feature.credits.ui.presentation.composable.loandetails.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.feature.credits.api.usecases.GetLoanDetailsUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class LoanDetailsViewModel @Inject constructor(
    private val getLoanDetailsUseCase: GetLoanDetailsUseCase,
) : MvvmViewModel<LoanDetailsState>(LoanDetailsState()) {

    fun loadDetails(loanId: String) {
        viewModelScope.launch {
            runCatching {
                internalState.update { it.copy(isLoading = true) }
                val details = getLoanDetailsUseCase(id = loanId)
                internalState.update {
                    it.copy(details = details.toUi())
                }
            }.onSuccess {
                internalState.update { it.copy(isLoading = false) }
            }.onFailure { error ->
                Log.d("LoanDetailsViewModel", "error", error)
                internalState.update { it.copy(isError = true) }
            }
        }
    }
}

@Immutable
internal data class LoanDetailsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val details: LoanDetailsUi? = null,
) : MvvmState


