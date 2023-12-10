package com.boostcamp.planj.ui

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PlanjFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("PLANJDEBUG", "PlanjFirebaseMessagingService onMessageReceived $message")
        super.onMessageReceived(message)
    }


    override fun onNewToken(token: String) {
        Log.d("PLANJDEBUG", "PlanjFirebaseMessagingService onNewToken $token")
        super.onNewToken(token)
    }

}