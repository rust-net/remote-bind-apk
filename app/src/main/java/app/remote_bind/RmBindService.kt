package app.remote_bind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService

class RmBindService : Service() {
    private val channelId: String = "msg"

    private fun getIntent() = Intent(this, RmBindActivity::class.java).let { notificationIntent ->
        PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    // https://developer.android.com/training/notify-user/build-notification?hl=zh-cn#kotlin
    private fun getNotification() = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("端口穿透")
        .setContentText("服务正在运行...")
        .setContentIntent(getIntent())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setShowWhen(true)
        .setAutoCancel(true)

        .setStyle(
            NotificationCompat.InboxStyle()
                .addLine("服务器")
                .addLine("0.0.0.0")
                .setBigContentTitle("服务状态")
                .setSummaryText("status")
        )
        .build()

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "服务状态通知"
            val descriptionText = "描述"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
            // more like
            getSystemService<NotificationManager>().also { notificationManager ->
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            // notificationId是一个唯一的int，对于每一个通知，你必须定义它。
            // 记得保存你传递给NotificationManagerCompat.notify()的通知ID，因为如果你想更新或删除通知，你以后会需要它。
//            this.notify(1, getNotification())
            startForeground(1, getNotification())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

}