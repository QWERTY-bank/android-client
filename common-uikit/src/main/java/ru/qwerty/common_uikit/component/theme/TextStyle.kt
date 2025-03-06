package ru.qwerty.common_uikit.component.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.R

/**
 * @author y.yangel
 */
object TextStyle {

    val SemiBoldStyle = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.poppins_semi_bold)
        ),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
    )

    val MediumFontStyle = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.poppins_medium)
        ),
        color = Colors.Leadbelcher,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    )

    val MediumFontGrayStyle = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.poppins_medium)
        ),
        color = Colors.Jet,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    )

    fun createMediumFontStyleWithColor(color: Color): TextStyle {
        return TextStyle(
            fontFamily = FontFamily(
                Font(R.font.poppins_medium)
            ),
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}


object Colors {
    val Leadbelcher = Color(202, 202, 202)
    val BlueDepression = Color(54, 41, 183)
    val Jet = Color(52, 52, 52)
    val JetBlack = Color(23, 23, 23)
    val SunshineSurprise = Color(255, 175, 42)
    val PeppyPeacock = Color(255, 66, 103)
    val SpanishGrey = Color(151, 151, 151)
}