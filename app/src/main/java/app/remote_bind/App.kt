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

    private val startForegroundService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        Application::startForegroundService else Application::startService
    var isServiceRunning = false
    fun toggleForegroundService(forceClose: Boolean = false) {
        val intent = Intent()
        intent.setClass(this, RmBindService::class.java)
        if (isServiceRunning) {
            stopService(intent)
            isServiceRunning = false
        } else if (!forceClose) {
            startForegroundService(this, intent)
            isServiceRunning = true
        }
    }
}