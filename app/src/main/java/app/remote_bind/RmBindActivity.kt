package app.remote_bind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.remote_bind.ui.theme.RemoteBindTheme

class RmBindActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemoteBindTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var handler: String? by rememberSaveable {
        mutableStateOf(null)
    }
    Column {
        Text(text = "Hello $name!")
        Button(onClick = {
            bridge.test()
        }) {
            Text(text = "TEST")
        }
        Button(onClick = {
            if (handler != null) {
                bridge.stop(handler!!)
                handler = null
            }
            handler = bridge.start("43.132.196.171:1234", 5555.toShort(), "test", "127.0.0.1:5555")
        }) {
            Text(text = if (handler != null) { "重新启动" } else { "启动" })
        }
        if (handler != null) {
            Button(onClick = {
                bridge.stop(handler!!)
                handler = null
            }) {
                Text(text = "停止")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RemoteBindTheme {
        Greeting("Android")
    }
}