package com.example.feature.account.ui.presentation.ui.accountlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.account.ui.R
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors.SpanishGrey
import ru.qwerty.common_uikit.component.theme.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
internal fun AccountListEmptyScreen(
    modifier: Modifier = Modifier,
    onCreateAccountTap: (String) -> Unit = {}
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.no_account),
            modifier = Modifier.fillMaxWidth(0.8f),
            style = TextStyle.MediumFontStyle,
            textAlign = TextAlign.Center,
            color = SpanishGrey,
            fontSize = 16.sp,
        )
        AccentButton(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(R.string.add_account),
            onClick = { showBottomSheet.value = true }
        )
        AddAccountBottomSheet(
            showBottomSheet = showBottomSheet,
            sheetState = sheetState,
            onCreateAccountTap = onCreateAccountTap,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddAccountBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>,
    sheetState: SheetState,
    onCreateAccountTap: (String) -> Unit
) {
    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = modifier.fillMaxHeight(),
            sheetState = sheetState,
            containerColor = Color.White,
            onDismissRequest = { showBottomSheet.value = false; }
        ) {
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                var text by remember { mutableStateOf("") }
                ComposeTextField(
                    onValueChange = { text = it },
                    textFieldValue = text,
                    placeHolderText = stringResource(R.string.add_account_name)
                )
                AccentButton(
                    modifier = Modifier.padding(top = 24.dp),
                    isEnabled = text.isNotBlank() && text.length > 5,
                    text = stringResource(R.string.add_account),
                    onClick = {
                        onCreateAccountTap(text)
                        showBottomSheet.value = false
                    }
                )
            }
        }
    }
}
