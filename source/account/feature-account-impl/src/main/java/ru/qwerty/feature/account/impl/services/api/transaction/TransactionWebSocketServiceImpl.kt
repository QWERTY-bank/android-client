package ru.qwerty.feature.account.impl.services.api.transaction

import android.util.Log
import eu.lepicekmichal.signalrkore.AutomaticReconnect
import eu.lepicekmichal.signalrkore.HubConnection
import eu.lepicekmichal.signalrkore.HubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnectionState
import eu.lepicekmichal.signalrkore.Logger
import eu.lepicekmichal.signalrkore.TransportEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.qwerty.common_network.common.CacheManager
import ru.qwerty.feature.account.api.domain.AccountBalance
import ru.qwerty.feature.account.api.domain.Transaction
import ru.qwerty.feature.account.impl.mapper.toDomain
import javax.inject.Inject
import javax.inject.Singleton

interface TransactionWebSocketService {

    suspend fun connect(accountId: String)

    suspend fun sendHandshake(accountId: String)

    suspend fun disconnect()

    suspend fun obtainTransactions(): Flow<Pair<AccountBalance, Transaction>>
}

@Singleton
internal class TransactionWebSocketServiceImpl @Inject constructor(
    private val cacheManager: CacheManager,
) : TransactionWebSocketService {

    private var webSocketConnection: HubConnection? = null

    private val _transactionsFlow = MutableSharedFlow<Pair<AccountBalance, Transaction>>(replay = 1)


    override suspend fun connect(accountId: String) {
        val token = cacheManager.getToken()

        webSocketConnection =
            HubConnectionBuilder.create("http://95.163.222.150:5001/account-hub?access_token=$token") {
                automaticReconnect = AutomaticReconnect.Active
                logger = Logger { severity, msg, cause ->
                    Log.d("SignalRKore is saying", "$severity, $msg, $cause")
                }
                transportEnum = TransportEnum.WebSockets
                skipNegotiate = true
            }

        webSocketConnection?.start()

        webSocketConnection?.connectionState?.collect {
            when (it) {
                HubConnectionState.CONNECTED -> {
                    sendHandshake(accountId)
                    Log.d(
                        "TransactionWebSocketServiceImpl",
                        "CONNECTED"
                    )
                }

                HubConnectionState.DISCONNECTED -> Log.d(
                    "TransactionWebSocketServiceImpl",
                    "DISCONNECTED"
                )

                HubConnectionState.CONNECTING -> Log.d(
                    "TransactionWebSocketServiceImpl",
                    "CONNECTING"
                )

                HubConnectionState.RECONNECTING -> Log.d(
                    "TransactionWebSocketServiceImpl",
                    "RECONNECTING"
                )
            }
        }
    }

    override suspend fun sendHandshake(accountId: String) {
        webSocketConnection?.send("SubscribeToAccount", SubscribeToAccountModel(accountId.toInt()))

        webSocketConnection?.on(
            target = "ReceiveTransaction",
            paramType1 = TransactionWebsocketResponse::class
        )?.collect { transactionApi ->
            Log.d("TransactionWebSocketServiceImpl", transactionApi.toString())
            _transactionsFlow.tryEmit(
                Pair(
                    first = transactionApi.arg1.accountValue.toDomain(),
                    second = transactionApi.arg1.transaction.toDomain()
                )
            )
            println(_transactionsFlow)
        }
    }

    override suspend fun obtainTransactions(): Flow<Pair<AccountBalance, Transaction>> {
        return _transactionsFlow.asSharedFlow()
    }

    override suspend fun disconnect() {
        webSocketConnection?.stop()
    }

}

@Serializable
internal data class SubscribeToAccountModel(
    @SerialName("accountId")
    val accountId: Int,
)


private fun AccountValue.toDomain(): AccountBalance {
    return AccountBalance(
        code = code,
        moneyAmount = value.toString(),
    )
}