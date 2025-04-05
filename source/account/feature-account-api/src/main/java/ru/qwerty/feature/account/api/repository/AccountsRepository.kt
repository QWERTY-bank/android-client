package ru.qwerty.feature.account.api.repository

import ru.qwerty.feature.account.api.domain.Account
import ru.qwerty.feature.account.api.domain.AccountTransactions
import ru.qwerty.feature.account.api.domain.Transaction

/**
 * @author y.yangel
 */
interface AccountsRepository {

    suspend fun isUserAlreadyLogged(): Boolean

    suspend fun getAccounts(): List<Account>

    suspend fun createAccount(title: String, amountType: String)

    suspend fun getAccountBalance(accountId: String): Double

    suspend fun getTransactions(id: String): AccountTransactions

    suspend fun deposit(id: String, depositAmount: String, key: String, code: String)

    suspend fun withdraw(id: String, withdrawAmount: String, key: String, code: String)

    suspend fun deleteAccount(id: String)

    suspend fun transferToAccount(
        fromAccountId: String,
        toAccountId: String,
        amount: String,
        key: String,
        code: String
    )
}