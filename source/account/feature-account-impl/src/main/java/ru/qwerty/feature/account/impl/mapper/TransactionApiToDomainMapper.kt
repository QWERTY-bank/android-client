package ru.qwerty.feature.account.impl.mapper

import ru.qwerty.feature.account.api.domain.AccountTransactions
import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.impl.services.api.transaction.AccountTransactionApi
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionApi

/**
 * @author y.yangel
 */
internal fun AccountTransactionApi.toDomain(): AccountTransactions {
    return AccountTransactions(
        transaction = transactions.map { it.toDomain() },
        depositCurrencyTotals = depositCurrencyTotals.value,
        withdrawCurrencyTotals = withdrawCurrencyTotals.value,
    )
}

internal fun TransactionApi.toDomain(): Transaction {
    return Transaction(
        key = key,
        type = type,
        moneyAmount = moneyAmountApis.toDomain()
    )
}