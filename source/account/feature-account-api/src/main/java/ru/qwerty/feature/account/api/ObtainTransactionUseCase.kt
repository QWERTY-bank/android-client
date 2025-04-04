package ru.qwerty.feature.account.api

import kotlinx.coroutines.flow.Flow
import ru.qwerty.feature.account.api.domain.AccountBalance
import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.api.repository.TransactionRepository

class ObtainTransactionsUseCase(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(): Flow<Pair<AccountBalance, Transaction>> {
        return transactionRepository.obtainTransaction()
    }

    suspend fun setupConnection(accountId: String) {
        transactionRepository.setupConnection(accountId)
    }

    suspend fun closeConnection() {
        transactionRepository.closeConnection()
    }
}
