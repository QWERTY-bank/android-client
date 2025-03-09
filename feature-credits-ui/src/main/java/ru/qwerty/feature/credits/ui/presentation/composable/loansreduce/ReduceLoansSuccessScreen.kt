package ru.qwerty.feature.credits.ui.presentation.composable.loansreduce

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors

@Composable
internal fun ReduceLoansSuccessScreen(
    modifier: Modifier = Modifier,
    onNavigateBackToLoans: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(ru.qwerty.common_uikit.R.drawable.tariff),
            modifier = Modifier.size(220.dp),
            contentDescription = ""
        )
        Text(
            text = "Транзакция успешна!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            style = TextStyle(
                fontFamily = LatoFont,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                letterSpacing = 1.sp,
                color = Colors.BlueDepression
            )
        )
        AccentButton(
            text = "Подтвердить",
            modifier = Modifier
                .padding(top = 24.dp),
            onClick = onNavigateBackToLoans,
        )
    }
}