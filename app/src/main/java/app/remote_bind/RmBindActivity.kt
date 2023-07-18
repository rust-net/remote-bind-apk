package app.remote_bind

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.remote_bind.ui.AppUI
import app.remote_bind.ui.theme.RemoteBindTheme
import lib.log

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