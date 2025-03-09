package ru.qwerty.feature.credits.ui.presentation.composable.loansflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import ru.qwerty.common_uikit.component.ComposeTextField
import ru.qwerty.common_uikit.component.LatoFont
import ru.qwerty.common_uikit.component.button.AccentButton
import ru.qwerty.common_uikit.component.theme.Colors
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.LoanApplyFlowNews
import ru.qwerty.feature.credits.ui.presentation.composable.loansflow.viewmodels.LoanApplyFlowViewModel

/**
 * @author y.yangel
 */
@Composable
internal fun LoanApplyFlowScreen(
    minDays: Int,
    maxDays: Int,
    tariffId: String,
    navigateToChooseAccount: (String, String, String) -> Unit,
    viewModel: LoanApplyFlowViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is LoanApplyFlowNews.AmountValidationError -> snackbarHostState.showSnackbar("Сумма должна быть более 1000")
                is LoanApplyFlowNews.PeriodValidationError -> snackbarHostState.showSnackbar("Некорректный период")
                is LoanApplyFlowNews.Error -> snackbarHostState.showSnackbar("Произошла ошибка, попробуйте позже")
                is LoanApplyFlowNews.NavigateToChooseAccount -> {
                    navigateToChooseAccount(news.amount, news.daysAmount, tariffId)
                }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ComposeTextField(
                textFieldValue = state.amount,
                onValueChange = viewModel::onAmountChanged,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeHolderText = "Сумма кредита",
            )
            Spacer(modifier = Modifier.height(20.dp))
            ComposeTextField(
                textFieldValue = state.period,
                placeHolderText = "Период",
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = viewModel::onPeriodChanged
            )
            Text(
                text = "Период тарифа $minDays до $maxDays дней",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = LatoFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp,
                    color = Colors.SpanishGrey
                )
            )
            AccentButton(
                modifier = Modifier.padding(top = 24.dp),
                onClick = { viewModel.applyMaxPeriod(maxPeriod = maxDays, minPeriod = minDays) },
                text = "Далее"
            )
        }
    }
}