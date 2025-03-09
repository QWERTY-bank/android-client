package com.example.feature.account.ui.presentation.ui.accountdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.theme.Colors

@Composable
internal fun AccountDetailsEmptyScreen(modifier: Modifier = Modifier) {
    AnimatedVisibility(true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Здесь будет история ваших операций",
                modifier = modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                    color = Colors.Jet
                )
            )
        }
    }
}