package ru.qwerty.common_uikit.component

import androidx.compose.foundation.Image
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
fun DepositTransactionCard(
    modifier: Modifier = Modifier,
    transactionDeposit: String = "5000",
    code: String = "Usd"
) {

    val type = when (code) {
        "Rub" -> "₽"
        "Usd" -> "$"
        else -> "€"
    }

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
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.deposit_icon),
                modifier = Modifier.size(32.dp),
                contentDescription = ""
            )
            Text(
                text = "Пополнение",
                modifier = Modifier
                    .padding(start = 16.dp),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Colors.JetBlack
                )
            )
            Text(
                text = "+$transactionDeposit $type",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Colors.BlueDepression
                )
            )
        }
    }
}

@Composable
@Preview
fun WithdrawTransactionCard(
    modifier: Modifier = Modifier,
    transactionWithdraw: String = "5000",
    code: String = "Eur"
) {

    val type = when (code) {
        "Rub" -> "₽"
        "Usd" -> "$"
        else -> "€"
    }

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
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.withdraw_icon),
                modifier = Modifier.size(32.dp),
                contentDescription = ""
            )
            Text(
                text = "Снятие",
                modifier = Modifier
                    .padding(start = 16.dp),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Colors.JetBlack
                )
            )
            Text(
                text = "-$transactionWithdraw $type",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Colors.PeppyPeacock
                )
            )
        }
    }
}