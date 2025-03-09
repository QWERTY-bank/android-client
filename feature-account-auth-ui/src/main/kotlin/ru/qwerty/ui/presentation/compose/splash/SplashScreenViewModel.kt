package ru.qwerty.ui.presentation.compose.splash

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.qwerty.feature_account_auth_api.IsUserAlreadyLoginUseCase
import javax.inject.Inject

/**
 * @author y.yangel
 */
@Stable
@HiltViewModel
internal class SplashScreenViewModel @Inject constructor(
    private val isUserAlreadyLoginUseCase: IsUserAlreadyLoginUseCase,
) : ViewModel() {

    private val _news = MutableSharedFlow<SplashScreenNews>(replay = 1)
    val news: SharedFlow<SplashScreenNews>
        get() = _news.asSharedFlow()

    init {
        viewModelScope.launch {
            val isUserAlreadyLogin = isUserAlreadyLoginUseCase()
            if (isUserAlreadyLogin) {
                _news.emit(SplashScreenNews.NavigateToAccountListScreenNews)
            } else {
                _news.emit(SplashScreenNews.NavigateToLoginScreenNews)
            }
        }
    }

}

internal sealed interface SplashScreenNews {
    data object NavigateToLoginScreenNews : SplashScreenNews
    data object NavigateToAccountListScreenNews : SplashScreenNews
}