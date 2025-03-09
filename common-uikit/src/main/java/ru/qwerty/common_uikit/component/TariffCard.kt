package ru.qwerty.common_uikit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.R
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
@Preview
fun TariffCard(
    modifier: Modifier = Modifier,
    name: String = "Летний",
    interestRate: Int = 17,
    interestRateType: String = "Annual",
    minPeriodDays: String = "1",
    maxPeriodDays: String = "11",
) {
    val interestRateTitle = when (interestRateType) {
        "Annual" -> "годовых"
        "Monthly" -> "ежемесячных"
        else -> "дневных"
    }

    val interestRateFullTitle = "под $interestRate% $interestRateTitle"

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = name,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp,
                        color = Colors.JetBlack
                    )
                )
                Text(
                    text = interestRateFullTitle,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
                Text(
                    text = "От $minPeriodDays от $maxPeriodDays дней",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.tariff),
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = ""
            )
        }
    }
}