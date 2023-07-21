package app.remote_bind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.remote_bind.ui.AppUI
import app.remote_bind.ui.theme.RemoteBindTheme

class RmBindActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemoteBindTheme {
                AppUI(
//                    configs = getConfigs(),
                )
            }
        }
    }
}