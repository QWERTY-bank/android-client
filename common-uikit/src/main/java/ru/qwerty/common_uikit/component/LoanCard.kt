package ru.qwerty.common_uikit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author y.yangel
 */
@Composable
@Preview
fun LoanCard(
    modifier: Modifier = Modifier,
    status: String = "ACTIVE",
    debtAmount: String = "170000",
    nextPaymentAmount: String = "5000",
    nextPaymentDate: String = "2025-03-09",
) {
    val statusLabel = remember {
        when (status) {
            "REQUESTED" -> "Ожидает одобрения"
            "ACTIVE" -> "Активен"
            "CLOSED" -> "Закрыт"
            else -> "Отклонен"
        }
    }

    val statusLabelColor = remember {
        when (status) {
            "REQUESTED" -> Colors.BlueDepression
            "ACTIVE" -> Colors.Peppy
            else -> Colors.PeppyPeacock
        }
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "Кредит",
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
                    text = statusLabel,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        color = statusLabelColor
                    )
                )
                Text(
                    text = "Сумма кредита $debtAmount ₽",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = Colors.Jet
                    )
                )
                Text(
                    text = "Следующая оплата $nextPaymentDate",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = Colors.Jet
                    )
                )
                Text(
                    text = "На сумму $nextPaymentAmount ₽",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = Colors.JetBlack
                    )
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.loan_label),
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = ""
            )
        }
    }
}


@Composable
@Preview
fun LoanDetailsCard(
    modifier: Modifier = Modifier,
    status: String = "ACTIVE",
    debtAmount: String = "170000",
    date: String = "2025-03-09",
) {
    val statusLabel = remember {
        when (status) {
            "Requested" -> "Ожидает одобрения"
            "Active" -> "Активен"
            "Closed" -> "Закрыт"
            else -> "Отклонен"
        }
    }

    val statusLabelColor = remember {
        when (status) {
            "Requested" -> Colors.BlueDepression
            "Active" -> Colors.Peppy
            else -> Colors.PeppyPeacock
        }
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "Кредит",
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
                    text = statusLabel,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        color = statusLabelColor
                    )
                )
                Text(
                    text = "Сумма кредита $debtAmount ₽",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = Colors.Jet
                    )
                )
                Text(
                    text = "Дата выдачи $date",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = Colors.Jet
                    )
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.loan_label),
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = ""
            )
        }
    }
}

@Composable
@Preview
fun NextPaymentCard(
    nextPaymentAmount: String = "5000",
    nextPaymentDate: String = "2025-03-09",
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier.fillMaxWidth()
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
                    text = "Следующая оплата",
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
                    text = nextPaymentDate,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
                Text(
                    text = "На сумму $nextPaymentAmount ₽",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Start,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.payment),
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = ""
            )
        }
    }
}

@Composable
@Preview
fun PaymentHistoryCard(
    paymentAmount: String = "5000",
    paymentDate: String = "2025-03-09T12:54:03.295Z",
    paymentStatus: String = "Conducted",
) {
    val paymentStatusLabel =
        when (paymentStatus) {
            "InProcess" -> "Оплачивается"
            "Conducted" -> "Оплачен"
            else -> "Отменен"
        }

    val paymentStatusColor =
        when (paymentStatus) {
            "InProcess" -> Colors.BlueDepression
            "Conducted" -> Colors.Peppy
            else -> Colors.PeppyPeacock
        }


    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier.fillMaxWidth()
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
                    text = "Досрочное погашение",
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
                    text = "$paymentAmount ₽",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
                Text(
                    text = paymentDate.toReadableDate(),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp,
                        color = Colors.SpanishGrey
                    )
                )
                Text(
                    text = paymentStatusLabel,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    style = TextStyle(
                        fontFamily = LatoFont,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp,
                        color = paymentStatusColor,
                    )
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.history),
                modifier = Modifier.padding(end = 8.dp),
                contentDescription = ""
            )
        }
    }
}


fun String.toReadableDate(): String {
    val instant = Instant.parse(this)
    val dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    return dateTime.format(formatter)
}