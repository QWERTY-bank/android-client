package com.example.feature.account.ui.domain.usecases

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author y.yangel
 */
class SendResultForAccountListFacade @Inject constructor() {

    private val sharedFlow by lazy { createSharedFlow() }

    suspend fun sendResultAfterTransaction() {
        sharedFlow.emit(Unit)
    }

    fun obtainTransactionEvent(): SharedFlow<Unit> {
        return sharedFlow.asSharedFlow()
    }

    private fun createSharedFlow(): MutableSharedFlow<Unit> {
        return MutableSharedFlow(replay = 1)
    }
}