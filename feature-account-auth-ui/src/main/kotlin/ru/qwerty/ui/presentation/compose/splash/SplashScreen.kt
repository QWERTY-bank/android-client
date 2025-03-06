package ru.qwerty.ui.presentation.compose.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.feature_account_auth_ui.R

/**
 * @author y.yangel
 */
@Composable
internal fun SplashScreen(
    viewModel: SplashScreenViewModel,
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToAccountList: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is SplashScreenNews.NavigateToLoginScreenNews -> onNavigateToLogin()
                is SplashScreenNews.NavigateToAccountListScreenNews -> onNavigateToAccountList()
            }
        }
    }
    SplashScreenInternal(modifier = modifier)
}

@Composable
private fun SplashScreenInternal(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.face_id),
            contentDescription = ""
        )
    }
}