package ru.qwerty.feature.account.api.repository

import kotlinx.coroutines.flow.Flow
import ru.qwerty.feature.account.api.domain.AccountBalance
import ru.qwerty.feature.account.api.domain.Transaction

interface TransactionRepository {

    suspend fun obtainTransaction(): Flow<Pair<AccountBalance, Transaction>>

    suspend fun setupConnection(accountId: String)

    suspend fun closeConnection()
}