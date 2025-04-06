package ru.qwerty.ui.presentation.compose

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.feature_account_auth_ui.R
import ru.qwerty.ui.viewmodel.AccountRemoteState
import ru.qwerty.ui.viewmodel.AccountState

@Composable
internal fun AccountScreenInternal(
    state: AccountState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(color = Colors.BlueDepression)) {
        Spacer(modifier = Modifier.size(150.dp))
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp
                    )
                )
                .background(Color.White)
        ) {
            Column(
                modifier = modifier
                    .background(color = Color.White)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Добро пожаловать!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Justify,
                        color = Colors.PeppyPeacock
                    ),
                    color = Colors.BlueDepression,
                    fontSize = 24.sp,
                )
                Animation()
                Text(
                    text = "Вход с QWERTY-ID — это быстрый, простой и более приватный способ авторизации в сторонних приложениях и на веб-сайтах с использованием вашей уже существующей учетной записи QWERTY.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 16.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W500,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Justify,
                        color = Colors.PeppyPeacock
                    ),
                    color = Colors.Jet,
                    fontSize = 16.sp,
                )
                SignUpBlock(state = state.accountRemoteState)
            }
        }
    }
}

@Composable
private fun Animation(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.lottie)
        )
        var isPlaying by remember {
            mutableStateOf(true)
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying
        )
        LaunchedEffect(key1 = progress) {
            if (progress == 0f) {
                isPlaying = true
            }
            if (progress == 1f) {
                isPlaying = false
            }
        }

        LottieAnimation(
            composition = composition,
            modifier = modifier
        )
    }
}

@Composable
private fun SignUpBlock(
    state: AccountRemoteState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    when {
        state.error -> AccountErrorState(modifier = modifier.padding(top = 24.dp))
        state.isLoading -> AccountLoadingState(modifier = modifier.padding(top = 24.dp))
    }
    HorizontalDivider(
        thickness = 2.dp,
    )
    AccentButton(
        modifier = Modifier.padding(top = 16.dp),
        onClick = { openLoginInCustomTab(context = context) },
        text = "Войти с помощью QWERTY-ID"
    )
}

fun openLoginInCustomTab(context: Context) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    val redirectUri = "mb-employee://auth/web"
    val loginUrl = "http://95.163.222.150:5004/auth/login?redirectUrl=$redirectUri"
    customTabsIntent.launchUrl(context, loginUrl.toUri())
}

@Composable
private fun AccountLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Colors.BlueDepression,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun AccountErrorState(modifier: Modifier = Modifier) {
    Text(
        text = "Произошла ошибка. Пожалуйста, попробуйте позже или обратитесь в поддержку",
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(
            fontFamily = LatoFont,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            color = Colors.PeppyPeacock
        )
    )
}

@Preview(locale = "ru")
@Composable
private fun AccountScreenInternalPreview() {
    AccountScreenInternal(state = AccountState())
}