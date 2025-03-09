package com.example.feature.account.ui.domain.usecases

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author y.yangel
 */
internal class SendResultAfterCloseAccountFacade @Inject constructor() {

    private val sharedFlow by lazy { createSharedFlow() }

    suspend fun sendResultCloseAccount() {
        sharedFlow.emit(Unit)
    }

    fun obtainResult(): SharedFlow<Unit> {
        return sharedFlow.asSharedFlow()
    }

    private fun createSharedFlow(): MutableSharedFlow<Unit> {
        return MutableSharedFlow(replay = 1)
    }
}