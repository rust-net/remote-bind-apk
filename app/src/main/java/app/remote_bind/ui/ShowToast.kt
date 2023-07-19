package app.remote_bind.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

private val default = Triple(false, "", Toast.LENGTH_SHORT)
private var toastStatus by mutableStateOf(default)

@Composable
fun ShowToast() {
    var toast: Toast? by remember { mutableStateOf(null) }
    val (show, message, length) = toastStatus
    if (show) {
        toast?.cancel()
        toast = Toast.makeText(LocalContext.current.applicationContext, message, length)
        toast?.show()
        toastStatus = default
    }
}

fun showToast(msg: String, length: Int = default.third) {
    toastStatus = Triple(true, msg, length)
}