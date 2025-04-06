package ru.qwerty.ui.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.qwerty.ui.viewmodel.AccountNews
import ru.qwerty.ui.viewmodel.AccountViewModel

/**
 * @author y.yangel
 */
@Composable
internal fun UserLoginScreen(
    viewModel: AccountViewModel,
    navigateOnAccountList: () -> Unit,
    code: String? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.news.collect {
            when (it) {
                is AccountNews.NavigateToAccountListScreenNews -> navigateOnAccountList()
            }
        }
    }

    LaunchedEffect(code) {
        if (code != null) {
            viewModel.loginUserByCode(code)
        }
    }

    AccountScreenInternal(
        state = state,
    )
}

