package ru.qwerty.feature.account.impl.services.api.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.qwerty.feature.account.impl.services.api.MoneyAmountApi

@Serializable
internal class AccountTransactionApi(
    val transactions: List<TransactionApi> = listOf(),
    val depositCurrencyTotals: List<DepositCurrencyTotals> = listOf(),
    val withdrawCurrencyTotals: List<WithdrawCurrencyTotals> = listOf(),
)

@Serializable
internal class TransactionRequest(
    val transaction: TransactionApi,
)

@Serializable
data class AccountBalanceRequest(
    val isClosed: Boolean,
    val currencyValues: List<CurrencyValue>,
)

@Serializable
data class CurrencyValue(
    val code: String,
    val value: Double,
)

@Serializable
internal class TransactionApi(
    val key: String? = null,
    val type: String? = null,
    @SerialName("currencyValues")
    val moneyAmountApis: List<MoneyAmountApi> = listOf(),
)

@Serializable
internal class DepositCurrencyTotals(
    val code: String? = null,
    val value: Double? = null,
)

@Serializable
internal class WithdrawCurrencyTotals(
    val code: String? = null,
    val value: Double? = null,
)
