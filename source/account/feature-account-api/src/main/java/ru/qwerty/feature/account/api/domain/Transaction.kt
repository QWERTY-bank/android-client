package ru.qwerty.feature.account.api.domain

class Transaction(
    val key: String? = null,
    val type: String? = null,
    val moneyAmount: MoneyAmount = MoneyAmount(),
)

class MoneyAmount(val value: Int? = null)
