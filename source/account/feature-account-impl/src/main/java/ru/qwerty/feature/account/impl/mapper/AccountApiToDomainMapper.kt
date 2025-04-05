package ru.qwerty.feature.account.impl.mapper

import ru.qwerty.feature.account.api.domain.Account
import ru.qwerty.feature.account.api.domain.MoneyAmount
import ru.qwerty.feature.account.impl.services.api.AccountApi
import ru.qwerty.feature.account.impl.services.api.MoneyAmountApi

/**
 * @author y.yangel
 */
internal fun AccountApi.toDomain(): Account {
    return Account(
        id = id,
        moneyAmount = moneyAmountApis.toDomain(),
        isClosed = isClosed,
        title = title,
        userId = userId,
    )
}

internal fun MoneyAmountApi.toDomain(): MoneyAmount {
    return MoneyAmount(value = value, code = code)
}