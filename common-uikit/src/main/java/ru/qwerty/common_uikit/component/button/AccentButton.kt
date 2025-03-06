package ru.qwerty.common_uikit.component.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.common_uikit.component.theme.Colors.BlueDepression
import ru.qwerty.common_uikit.component.theme.TextStyle

/**
 * @author y.yangel
 */
@Composable
fun AccentButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = BlueDepression),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            style = TextStyle.MediumFontStyle,
            color = Color.White,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun AccentButtonChip(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    containerColor: Color = BlueDepression,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = CircleShape,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            fontFamily = LatoFont,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun AccentButtonPreview() {
    Column {
        AccentButton(text = "Sign up")
        AccentButtonChip(text = "+")
    }
}