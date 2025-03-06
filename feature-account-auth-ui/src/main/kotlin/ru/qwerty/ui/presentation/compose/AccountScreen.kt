package ru.qwerty.ui.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.TextStyle.MediumFontStyle
import ru.qwerty.feature_account_auth_ui.R
import ru.qwerty.ui.viewmodel.AccountNews
import ru.qwerty.ui.viewmodel.AccountViewModel

/**
 * @author y.yangel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserLoginScreen(viewModel: AccountViewModel, navigateOnAccountList: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                is AccountNews.AccountNewsSnackbar -> {
                    snackbarHostState.showSnackbar(message = context.getString(R.string.snackbar_label))
                }

                is AccountNews.NavigateToAccountListScreenNews -> navigateOnAccountList()
            }
        }
    }
    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Text(
                        text = stringResource(R.string.account_app_bar_label),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        style = MediumFontStyle,
                        color = Colors.BlueDepression,
                        fontSize = 16.sp,
                    )
                })
        }
    ) { contentPadding ->
        AccountScreenInternal(
            state = state,
            modifier = Modifier.padding(contentPadding),
            onPasswordChange = viewModel::onPasswordInput,
            onPhoneChange = viewModel::onPhoneInput,
            onButtonClicked = viewModel::loginUserByPhone,
        )
    }

}

