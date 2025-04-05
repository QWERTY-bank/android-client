package ru.qwerty.feature.account.impl.repository

import kotlinx.coroutines.flow.Flow
import ru.qwerty.feature.account.api.domain.AccountBalance
import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.api.repository.TransactionRepository
import ru.qwerty.feature.account.impl.services.api.transaction.TransactionWebSocketService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TransactionRepositoryImpl @Inject constructor(
    private val transactionWebSocketService: TransactionWebSocketService,
) : TransactionRepository {

    override suspend fun setupConnection(accountId: String) {
        return transactionWebSocketService.connect(accountId)
    }

    override suspend fun obtainTransaction(): Flow<Pair<AccountBalance, Transaction>> {
        return transactionWebSocketService.obtainTransactions()
    }

    override suspend fun closeConnection() {
        transactionWebSocketService.disconnect()
    }

}