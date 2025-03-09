package ru.qwerty.ui.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.ComposePasswordTextField
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.TextStyle.MediumFontStyle
import ru.qwerty.feature_account_auth_ui.R
import ru.qwerty.ui.viewmodel.AccountRemoteState
import ru.qwerty.ui.viewmodel.AccountState

@Composable
internal fun AccountScreenInternal(
    state: AccountState,
    modifier: Modifier = Modifier,
    onPhoneChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.illustration),
            modifier = Modifier.padding(bottom = 32.dp),
            contentDescription = ""
        )
        ComposeTextField(
            textFieldValue = state.accountUiState.phone,
            onValueChange = onPhoneChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            placeHolderText = stringResource(R.string.phone),
        )
        Spacer(modifier = Modifier.height(20.dp))
        ComposePasswordTextField(
            textFieldValue = state.accountUiState.password,
            placeHolderText = stringResource(R.string.password),
            onValueChange = onPasswordChange
        )
        SignUpBlock(state = state.accountRemoteState, onButtonClicked = onButtonClicked)
    }
}

@Composable
private fun SignUpBlock(state: AccountRemoteState, modifier: Modifier = Modifier, onButtonClicked: () -> Unit = {}) {
    Text(
        text = stringResource(R.string.forgot_password_label),
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 4.dp, top = 12.dp),
        style = MediumFontStyle,
        textAlign = TextAlign.End
    )
    when {
        state.error -> AccountErrorState(modifier = Modifier.padding(top = 16.dp))
        state.isLoading -> AccountLoadingState(modifier = Modifier.padding(top = 16.dp))
    }
    AccentButton(
        modifier = Modifier.padding(top = 64.dp),
        onClick = onButtonClicked,
        text = stringResource(R.string.account_button_label)
    )
    Image(
        painter = painterResource(R.drawable.fingerprint),
        modifier = Modifier.padding(top = 24.dp),
        contentDescription = ""
    )
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

@Preview
@Composable
private fun AccountScreenInternalPreview() {
    AccountScreenInternal(state = AccountState())
}