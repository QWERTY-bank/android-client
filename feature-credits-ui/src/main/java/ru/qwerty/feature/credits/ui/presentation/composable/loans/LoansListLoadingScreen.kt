package ru.qwerty.feature.credits.ui.presentation.composable.loans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.qwerty.common_uikit.component.theme.Colors

/**
 * @author y.yangel
 */
@Composable
internal fun LoansListLoadingScreen(modifier: Modifier = Modifier)  {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Colors.BlueDepression,
            modifier = Modifier.size(64.dp)
        )
    }
}