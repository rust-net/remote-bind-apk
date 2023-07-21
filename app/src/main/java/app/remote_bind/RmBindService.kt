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
import lib.log

var instRmBindService: RmBindService? = null

class RmBindService : Service() {
    private val channelId = "msg"
    private val id = 1

    private val intent by lazy {
        Intent(this, RmBindActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    // https://developer.android.com/training/notify-user/build-notification?hl=zh-cn#kotlin
    private val notification by lazy {
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("端口穿透")
            .setContentText("服务正在运行...")
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setStyle(
                getStyle()
            )
    }

    private fun getStyle() = NotificationCompat.InboxStyle()
        .setSummaryText("status")
        .setBigContentTitle("服务已启动：").also { inbox ->
            // 思考：该状态应如何同步？
            runningList.toList().forEach { (name, _) ->
                inbox.addLine(name)
            }
        }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "服务状态通知"
            val descriptionText = "描述：该通知使应用程序服务置于前台"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            // val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // notificationManager.createNotificationChannel(channel)
            // more like
            getSystemService<NotificationManager>().also { notificationManager ->
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        instRmBindService = this
        createNotificationChannel()
        startForeground(id, notification.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    fun update() {
        // NotificationManagerCompat.from(this).apply {
            // notificationId是一个唯一的int，对于每一个通知，你必须定义它。
            // 记得保存你传递给NotificationManagerCompat.notify()的通知ID，因为如果你想更新或删除通知，你以后会需要它。
            // 普通的发送通知，需要权限：android.permission.POST_NOTIFICATIONS
            // this.notify(id, notification.setStyle(getStyle()).build())
        // }
        startForeground(id, notification.setStyle(getStyle()).build())
    }
}