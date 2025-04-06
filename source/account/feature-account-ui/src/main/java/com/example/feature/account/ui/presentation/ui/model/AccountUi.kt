package com.example.feature.account.ui.presentation.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.qwerty.feature.account.api.domain.Account
import ru.qwerty.feature.account.api.domain.AccountTransactions
import ru.qwerty.feature.account.api.domain.Transaction

@Immutable
data class AccountUi(
    val id: Int,
    val moneyAmount: String,
    val isClosed: Boolean,
    val title: String,
    val userId: String,
    val accountCurrencyType: String,
    val isHidden: Boolean = false,
)

@Immutable
data class AccountTransactionsUi(
    val transactions: ImmutableList<TransactionUi> = persistentListOf(),
    val depositCurrencyTotals: Double? = null,
    val withdrawCurrencyTotals: Double? = null
)

@Immutable
data class TransactionUi(
    val key: String? = null,
    val type: String? = null,
    val moneyAmount: Double = 0.0,
    val code: String?,
)

internal fun Account.toUi(hiddenAccount: List<Int>): AccountUi {
    return AccountUi(
        id = this.id,
        moneyAmount = this.moneyAmount.value.toString(),
        isClosed = this.isClosed,
        title = this.title,
        userId = this.userId,
        accountCurrencyType = moneyAmount.code ?: "Rub",
        isHidden = hiddenAccount.contains(this.id),
    )
}

internal fun AccountTransactions.toUi(): AccountTransactionsUi {
    return AccountTransactionsUi(
        transactions = this.transaction.map { it.toUi() }.toImmutableList(),
        depositCurrencyTotals = this.depositCurrencyTotals,
        withdrawCurrencyTotals = this.withdrawCurrencyTotals
    )
}

internal fun Transaction.toUi(): TransactionUi {
    return TransactionUi(
        key = key,
        type = type,
        moneyAmount = moneyAmount.value ?: 0.0,
        code = moneyAmount.code,
    )
}