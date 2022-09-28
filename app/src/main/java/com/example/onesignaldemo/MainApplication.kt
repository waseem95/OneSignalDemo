package com.example.onesignaldemo

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.onesignal.OneSignal.OSNotificationOpenedHandler

const val ONESIGNAL_APP_ID = "YOUR_ONE_SIGNAL_APP_ID" // add your one signal app id here

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler(this))
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
    }
}

internal class NotificationOpenedHandler(context2: MainApplication) :
    OSNotificationOpenedHandler {
    private val context2: Context
    override fun notificationOpened(result: OSNotificationOpenedResult) {
        val value: String
        val data = result.notification.additionalData
        Log.e("AdditionalData", "data : ${result.notification.additionalData}")
        val launchUrl = result.notification.launchURL
        val intent: Intent
        if (launchUrl != null) {
            intent = Intent(context2, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("url", launchUrl)
        } else {
            intent = Intent(context2, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            if (data != null) {
                if (data.has("webview")) {
                    value = data.optString("webview", null)
                    intent.putExtra("sectionno", 1)
                    intent.putExtra("notifyparam", value)
                }
            }
        }
        context2.startActivity(intent)
    }

    init {
        this.context2 = context2
    }
}
