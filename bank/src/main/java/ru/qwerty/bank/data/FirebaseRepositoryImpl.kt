package ru.qwerty.bank.data

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await
import ru.qwerty.feature_account_auth_api.repository.FirebaseRepository

/**
 * @author y.yangel
 */
internal class FirebaseRepositoryImpl : FirebaseRepository {

    override suspend fun obtainFirebaseToken() {
        val token = Firebase.messaging.token.await()
        Log.d("FirebaseRepositoryImpl", "Firebase token: $token")
    }
}