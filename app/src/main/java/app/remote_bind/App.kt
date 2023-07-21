package app.remote_bind

import android.app.Application
import android.content.Intent
import android.os.Build

lateinit var application: App

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
    }

    fun startForegroundService() {
        val intent = Intent()
        intent.setClass(this, RmBindService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }
    }
}