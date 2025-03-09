package ru.qwerty.feature.account.impl.mapper

import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.impl.services.api.MoneyAmountApi
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionApi

/**
 * @author y.yangel
 */
internal fun Transaction.toApi(): TransactionApi {
    return TransactionApi(
        key = key,
        type = type,
        moneyAmountApis = listOf(MoneyAmountApi(value = moneyAmount.value, code = "Rub"))
    )
}