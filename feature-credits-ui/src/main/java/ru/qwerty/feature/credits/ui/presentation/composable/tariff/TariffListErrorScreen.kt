package ru.qwerty.feature.credits.ui.presentation.composable.tariff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun TariffListErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Произошла ошибка во время\nвыполнения вашего запроса",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                color = Colors.PeppyPeacock
            )
        )
        AccentButton(
            text = "Повторить",
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetryClick,
        )
    }
}