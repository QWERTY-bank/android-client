package ru.qwerty.feature.account.api.domain

class Account(
    val id: Int,
    val moneyAmount: MoneyAmount,
    val isClosed: Boolean,
    val title: String,
    val userId: String,
)

class AccountTransactions(
    val transaction: List<Transaction> = listOf(),
    val depositCurrencyTotals: Int? = null,
    val withdrawCurrencyTotals: Int? = null,
)