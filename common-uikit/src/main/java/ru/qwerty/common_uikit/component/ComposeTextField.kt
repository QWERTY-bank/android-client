package ru.qwerty.common_uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.qwerty.common_uikit.R
import ru.qwerty.common_uikit.component.theme.Colors.JetBlack
import ru.qwerty.common_uikit.component.theme.TextStyle.MediumFontGrayStyle
import ru.qwerty.common_uikit.component.theme.TextStyle.MediumFontStyle

/**
 * @author y.yangel
 */
@Composable
fun ComposeTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String = "",
    placeHolderText: String = "",
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val outlinedColor = Color.Gray
    val containerColor = Color.Transparent
    BasicTextField(
        modifier = Modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = outlinedColor,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        textStyle = MediumFontGrayStyle,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        enabled = true,
        cursorBrush = SolidColor(JetBlack),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                innerTextField()
                if (textFieldValue.isEmpty()) {
                    Text(
                        text = placeHolderText,
                        style = MediumFontStyle,
                        color = JetBlack
                    )
                }
            }
        }
    )
}


@Composable
fun ComposePasswordTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String = "",
    singleLine: Boolean = true,
    placeHolderText: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val outlinedColor = Color.Gray
    val containerColor = Color.Transparent
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    BasicTextField(
        modifier = Modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = outlinedColor,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = onValueChange,
        visualTransformation = if (!passwordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        textStyle = MediumFontGrayStyle,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        enabled = true,
        cursorBrush = SolidColor(Color.Gray),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                innerTextField()
                if (textFieldValue.isEmpty()) {
                    Text(
                        text = placeHolderText,
                        style = MediumFontStyle,
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.vector),
                    modifier = Modifier
                        .clickable(onClick = {
                            passwordVisibility = !passwordVisibility
                        })
                        .size(12.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = null,
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ComposeTextFieldPreview() {
    ComposeTextField(
        textFieldValue = "Text Input"
    )
}