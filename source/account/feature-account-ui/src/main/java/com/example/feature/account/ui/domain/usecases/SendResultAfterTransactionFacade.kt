package com.example.feature.account.ui.domain.usecases

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author y.yangel
 */
internal class SendResultAfterTransactionFacade @Inject constructor(){

    private val sharedFlow by lazy { createSharedFlow() }

    suspend fun sendResultAfterTransaction(id: String) {
        sharedFlow.emit(id)
    }

    fun obtainTransactionEvent() : SharedFlow<String> {
        return sharedFlow.asSharedFlow()
    }

    private fun createSharedFlow(): MutableSharedFlow<String> {
        return MutableSharedFlow(replay = 1)
    }
}