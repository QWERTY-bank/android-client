package ru.qwerty.feature.account.impl.services.api.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.qwerty.feature.account.impl.services.api.MoneyAmountApi

@Serializable
internal class AccountTransactionApi(
    val transactions: List<TransactionApi> = listOf(),
    val depositCurrencyTotals: DepositCurrencyTotals,
    val withdrawCurrencyTotals: WithdrawCurrencyTotals,
)

@Serializable
internal class TransactionRequest(
    val transaction: TransactionApi,
)

@Serializable
data class AccountBalanceRequest(
    val isClosed: Boolean,
    val currencyValue: CurrencyValue,
)


@Serializable
internal data class TransactionWebsocketResponse(
    @SerialName("currencyValue")
    val accountValue: AccountValue,
    @SerialName("transaction")
    val transaction: TransactionApi,
)

@Serializable
internal data class AccountValue(
    val code: String,
    val value: Double,
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
    @SerialName("currencyValue")
    val moneyAmountApis: MoneyAmountApi,
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
