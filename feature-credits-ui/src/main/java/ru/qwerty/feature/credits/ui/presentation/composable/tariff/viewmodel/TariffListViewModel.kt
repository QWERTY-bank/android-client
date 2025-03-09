package ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel

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
import ru.qwerty.feature.credits.api.domain.CreditType
import ru.qwerty.feature.credits.api.usecases.GetTariffUseCase
import ru.qwerty.feature.credits.ui.mappers.toUi
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class TariffListViewModel @Inject constructor(
    private val getTariffUseCase: GetTariffUseCase
) : MvvmViewModel<TariffListState>(TariffListState()) {

    val state: StateFlow<TariffListState>
        get() = internalState.asStateFlow()

    init {
        loadTariffs()
    }

    fun loadTariffs() {
        viewModelScope.launch {
            runCatching {
                internalState.update { state -> state.copy(isLoading = true) }
                val tariffList = getTariffUseCase()
                internalState.update { state ->
                    state.copy(tariffList = tariffList.map { it.toUi() }.toPersistentList())
                }
            }.onSuccess {
                internalState.update { state -> state.copy(isLoading = false) }
            }.onFailure {
                internalState.update { state -> state.copy(isError = false) }
            }
        }
    }
}

@Immutable
internal data class TariffListState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val tariffList: ImmutableList<TariffUi> = persistentListOf()
) : MvvmState

@Immutable
internal data class TariffUi(
    val id: String,
    val name: String,
    val interestRate: Int,
    val interestRateType: CreditType,
    val minPeriodDays: Int,
    val maxPeriodDays: Int,
)