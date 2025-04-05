package ru.qwerty.feature.account.impl.services.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class AccountApi(
    @SerialName("currencyValue")
    val moneyAmountApis: MoneyAmountApi,
    @SerialName("id")
    val id: Int,
    @SerialName("isClosed")
    val isClosed: Boolean,
    @SerialName("title")
    val title: String,
    @SerialName("userId")
    val userId: String,
)

@Serializable
internal class MoneyAmountApi(
    @SerialName("code")
    val code: String,
    @SerialName("value")
    val value: Double?,
)