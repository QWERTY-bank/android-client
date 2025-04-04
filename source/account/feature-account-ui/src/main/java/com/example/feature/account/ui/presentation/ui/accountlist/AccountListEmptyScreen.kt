package com.example.feature.account.ui.presentation.ui.accountlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.account.ui.R
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors.SpanishGrey
import ru.qwerty.common_uikit.component.theme.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
internal fun AccountListEmptyScreen(
    modifier: Modifier = Modifier,
    onCreateAccountTap: (String, String) -> Unit = { _, _ -> }
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun AddAccountBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>,
    sheetState: SheetState,
    onCreateAccountTap: (String, String) -> Unit,
) {
    val amountTypeSelected = remember { mutableStateOf("") }

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
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    AmountTypeItem(
                        text = "Рублевый ₽",
                        onCheckedChange = { amountTypeSelected.value = if (it) "₽" else "" },
                        isChecked = amountTypeSelected.value == "₽"
                    )
                    AmountTypeItem(
                        text = "Долларовый $",
                        onCheckedChange = { amountTypeSelected.value = if (it) "$" else "" },
                        isChecked = amountTypeSelected.value == "$"
                    )
                    AmountTypeItem(
                        text = "Евро €",
                        onCheckedChange = { amountTypeSelected.value = if (it) "€" else "" },
                        isChecked = amountTypeSelected.value == "€"
                    )
                }
                AccentButton(
                    modifier = Modifier.padding(top = 24.dp),
                    isEnabled = text.isNotBlank() && text.length > 5 && amountTypeSelected.value.isNotBlank(),
                    text = stringResource(R.string.add_account),
                    onClick = {
                        onCreateAccountTap(text, amountTypeSelected.value)
                        showBottomSheet.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AmountTypeItem(
    text: String = "Рублевый ₽",
    onCheckedChange: (Boolean) -> Unit,
    isChecked: Boolean = false,
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Text(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.W400,
                fontStyle = FontStyle.Normal,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheetPreview() {
    val showBottomSheet = remember { mutableStateOf(false) }
    AddAccountBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = rememberModalBottomSheetState(),
        onCreateAccountTap = { _, _ -> }
    )
}
