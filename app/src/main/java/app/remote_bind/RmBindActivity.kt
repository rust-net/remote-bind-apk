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
    private val sp: SharedPreferences by lazy {
        getSharedPreferences("config", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val configs by remember {
                mutableStateOf(getConfigs(sp))
            }
            log.i(configs)
            RemoteBindTheme {
                AppUI(
                    configs = configs,
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