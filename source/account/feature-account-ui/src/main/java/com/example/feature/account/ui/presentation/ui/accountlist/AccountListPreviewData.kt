package com.example.feature.account.ui.presentation.ui.accountlist

import com.example.feature.account.ui.presentation.ui.model.AccountUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object AccountListPreviewData {

    val accountList: ImmutableList<AccountUi> = persistentListOf(
        AccountUi(
            id = 1,
            moneyAmount = "150",
            isClosed = false,
            title = "Счет для сбережений",
            userId = "user1",
            accountCurrencyType = "Rub",
        ),
        AccountUi(
            id = 2,
            moneyAmount = "250",
            isClosed = true,
            title = "Счет для путешествий",
            userId = "user2",
            accountCurrencyType = "Euro",
        ),
        AccountUi(
            id = 3,
            moneyAmount = "32550",
            isClosed = false,
            title = "Кредитный счет",
            userId = "user3",
            accountCurrencyType = "Dollar",
        ),
        AccountUi(
            id = 4,
            moneyAmount = "980",
            isClosed = false,
            title = "Счет для покупок",
            userId = "user4",
            accountCurrencyType = "Rub",
        ),
        AccountUi(
            id = 5,
            moneyAmount = "4500",
            isClosed = true,
            title = "Счет для бизнеса",
            userId = "user5",
            accountCurrencyType = "Rub",
        )
    )
}