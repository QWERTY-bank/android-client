package com.example.feature.account.ui.presentation.ui.transfer.externaltransfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.feature.account.ui.R
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.button.AccentButton

@Composable
internal fun ExternalTransferScreen(
    modifier: Modifier = Modifier,
    fromAccountId: String,
    moneyAmount: String,
    code: String,
    onNavigateToSelfActions: (String, String, String, String) -> Unit,
) {
    val remember = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.deposit),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(24.dp))
        ComposeTextField(
            placeHolderText = "Введите номер счета",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textFieldValue = remember.value,
            onValueChange = { remember.value = it }
        )
        AccentButton(
            text = "Продолжить",
            modifier = Modifier.padding(top = 8.dp),
            isEnabled = remember.value.isNotBlank(),
            onClick = { onNavigateToSelfActions(fromAccountId, remember.value, moneyAmount, code) }
        )
    }
}