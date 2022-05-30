package com.group25.timebanking.service

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())

        Log.d("MyFirebaseMessagingService", "New push notification received")

        handler.post(Runnable {
            Toast.makeText(
                baseContext, "You received a new request",
                Toast.LENGTH_LONG
            ).show()
        })
    }
}