package ru.qwerty.feature.credits.ui.presentation.composable.loans.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.example.common_mvvm.MvvmState
import com.example.common_mvvm.MvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.qwerty.feature.credits.api.domain.Loan
import ru.qwerty.feature.credits.api.domain.LoanStatus
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.feature.credits.api.usecases.GetLoansUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class LoansListViewModel @Inject constructor(
    private val getLoansUseCase: GetLoansUseCase,
    private val creditsRepository: CreditsRepository,
) : MvvmViewModel<LoanListState>(LoanListState()) {

    val state: StateFlow<LoanListState>
        get() = internalState.asStateFlow()

    init {
        loadLoans()
    }

    fun loadLoans() {
        viewModelScope.launch {
            runCatching {
                internalState.update { state -> state.copy(isLoading = true) }
                val loans = getLoansUseCase()
                val creditRating = creditsRepository.loadCreditRating()
                internalState.update { state ->
                    state.copy(
                        loanList = loans.map { it.toUi() }.toPersistentList(),
                        isLoading = false,
                        rating = creditRating.first,
                        maxRating = creditRating.second,
                    )
                }
            }.onSuccess {
                internalState.update { state -> state.copy(isLoading = false) }
            }.onFailure { error ->
                internalState.update { state -> state.copy(isError = true) }
                internalState.update { state -> state.copy(isLoading = false) }
                Log.d("LoansListViewModel", "error", error)
            }
        }
    }
}

@Immutable
internal data class LoanListState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val rating: Int = 0,
    val maxRating: Int = 100,
    val loanList: ImmutableList<LoanUi> = persistentListOf()
) : MvvmState

@Immutable
internal data class LoanUi(
    val id: String,
    val status: LoanStatus,
    val debtAmount: String,
    val nextPaymentAmount: String,
    val nextPaymentDate: String,
)

private fun Loan.toUi(): LoanUi {
    return LoanUi(
        id = id,
        status = status,
        debtAmount = "$debtAmount",
        nextPaymentAmount = "$nextPaymentAmount",
        nextPaymentDate = nextPaymentDateOnly,
    )
}
