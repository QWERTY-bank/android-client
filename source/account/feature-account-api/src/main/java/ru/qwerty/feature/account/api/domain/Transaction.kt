package ru.qwerty.feature.account.api.domain

class Transaction(
    val key: String? = null,
    val type: String? = null,
    val moneyAmount: MoneyAmount = MoneyAmount(),
)

class MoneyAmount(val value: Double? = null, val code: String? = null)

class AccountBalance(
    val code: String? = null,
    val moneyAmount: String? = null,
)
