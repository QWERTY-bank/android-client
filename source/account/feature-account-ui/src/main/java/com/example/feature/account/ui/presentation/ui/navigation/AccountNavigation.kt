package com.example.feature.account.ui.presentation.ui.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * @author y.yangel
 */
@Serializable
@Immutable
data class AccountNavigation(
    val id: Int,
    val title: String,
    val isClosed: Boolean,
    val moneyAmount: String,
    val accountCurrencyType: String,
    val red: Float,
    val green: Float,
    val blue: Float,
)

@Serializable
@Immutable
data class WithdrawNavigation(
    val accountId: String,
    val currentBalance: String,
    val accountCurrencyType: String,
)

@Serializable
@Immutable
data class DepositNavigation(val accountId: String, val accountCurrencyType: String)