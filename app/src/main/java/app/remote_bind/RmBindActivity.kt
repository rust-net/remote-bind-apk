package app.remote_bind

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.remote_bind.ui.AppUI
import app.remote_bind.ui.theme.RemoteBindTheme

class RmBindActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE)
        setContent {
            RemoteBindTheme {
                AppUI(
//                    configs = getConfigs(),
                )
            }
        }
    }

    private fun startService() {
        val intent = Intent()
        intent.setClass(this, RmBindService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }
    }
}