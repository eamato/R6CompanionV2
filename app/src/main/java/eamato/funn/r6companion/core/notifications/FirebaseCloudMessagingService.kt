package eamato.funn.r6companion.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val notification = p0.notification
        val title = p0.data["title"] ?: notification?.title
        val body = p0.data["body"] ?: notification?.body
        R6NotificationManager.showNotification(
            context = this,
            notificationId = 1,
            title = title,
            content = body
        )
    }

}