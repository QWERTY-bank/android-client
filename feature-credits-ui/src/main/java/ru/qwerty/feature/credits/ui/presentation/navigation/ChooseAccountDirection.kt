package ru.qwerty.feature.credits.ui.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
internal class ChooseAccountDirection(
    val amount: String,
    val daysAmount: String,
    val tariffId: String,
)

@Serializable
internal class LoanDetailsDirection(
    val loanId: String,
)

@Serializable
internal class LoansReduceChooseAccountDirection(
    val creditId: String,
)

@Serializable
internal class DepositForLoanScreenDirection(
    val accountMoneyAmount: String,
    val accountId: Int,
    val creditId: String,
)