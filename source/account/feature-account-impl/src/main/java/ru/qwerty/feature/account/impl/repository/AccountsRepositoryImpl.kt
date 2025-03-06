package ru.qwerty.feature.account.impl.repository

import android.util.Log
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature.account.api.domain.Account
import ru.qwerty.feature.account.api.domain.AccountTransactions
import ru.qwerty.feature.account.api.repository.AccountsRepository
import ru.qwerty.feature.account.impl.mapper.toDomain
import ru.qwerty.feature.account.impl.services.AccountsService
import ru.qwerty.feature.account.impl.services.CreateAccount
import ru.qwerty.feature.account.impl.services.api.MoneyAmountApi
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionApi
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionRequest
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * @author y.yangel
 */
internal class AccountsRepositoryImpl @Inject constructor(
    private val accountsService: AccountsService,
    private val cacheManager: CacheManager,
) : AccountsRepository {

    override suspend fun isUserAlreadyLogged(): Boolean {
        return cacheManager.getToken() != null
    }

    override suspend fun getAccounts(): List<Account> {
        return try {
            val accounts = accountsService.getAccounts()
            Log.d("AccountsService", "Received accounts: $accounts")
            accounts.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("AccountsService", "Error fetching accounts: ${e.message}", e)
            throw e
        }
    }

    override suspend fun createAccount(title: String) {
        accountsService.createAccount(createAccount = CreateAccount(title = title))
        Log.d("AccountsService", "creation success")
    }

    override suspend fun getAccountBalance(accountId: String): Int {
        val accountBalance = accountsService
            .getAccountBalance(accountId.toInt())
            .currencyValues
            .first()
            .value
        return accountBalance
    }

    override suspend fun getTransactions(id: String): AccountTransactions {
        val result = accountsService.getTransaction(id = id.toInt())
        return result.toDomain()
    }

    override suspend fun deposit(id: String, depositAmount: String) {
        return accountsService.deposit(
            id = id.toInt(),
            transactionRequest = TransactionRequest(createTransaction(depositAmount)),
        )
    }

    override suspend fun withdraw(id: String, withdrawAmount: String) {
        return accountsService.withdraw(
            id = id.toInt(),
            transactionRequest = TransactionRequest(createTransaction(withdrawAmount)),
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun createTransaction(withdrawAmount: String): TransactionApi {
        val transaction = TransactionApi(
            key = Uuid.random().toString(),
            moneyAmountApis = listOf(MoneyAmountApi(code = "Rub", value = withdrawAmount.toInt()))
        )
        return transaction
    }

    override suspend fun deleteAccount(id: String) {
        return accountsService.deleteAccount(id = id.toInt())
    }
}