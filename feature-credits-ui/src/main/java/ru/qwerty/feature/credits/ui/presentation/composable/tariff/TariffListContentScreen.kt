package ru.qwerty.feature.credits.ui.presentation.composable.tariff

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.TariffCard
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.feature.credits.ui.presentation.composable.tariff.viewmodel.TariffListState

/**
 * @author y.yangel
 */
@Composable
internal fun TariffListContentScreen(
    state: TariffListState,
    modifier: Modifier = Modifier,
    onTariffTap: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
    ) {
        item {
            Text(
                text = "Выберите интересующий вас тариф",
                modifier = modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Colors.JetBlack,
                    letterSpacing = 1.sp,
                )
            )
        }
        items(state.tariffList) { item ->
            TariffCard(
                modifier = modifier.clickable { onTariffTap(item.id) },
                name = item.name,
                interestRate = item.interestRate,
                interestRateType = item.interestRateType.name,
                minPeriodDays = item.minPeriodDays.toString(),
                maxPeriodDays = item.maxPeriodDays.toString(),
            )
        }
    }
}