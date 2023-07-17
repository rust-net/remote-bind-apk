package app.remote_bind.ui

import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import app.remote_bind.Instance
import app.remote_bind.Server

@Composable
fun ConfigDialog(
    showDialog: MutableState<Boolean>,
    server: Server? = null,
    instance: Instance? = null,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.onBackground,
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            if (server != null) {
                Text("编辑服务器", color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("编辑配置", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        text = {
            Text("我是内容", color = MaterialTheme.colorScheme.onPrimary)
        },
        confirmButton = {
            TextButton(onClick = {}) {
                Text("确定", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text("取消", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            securePolicy = SecureFlagPolicy.SecureOn
        ),
        modifier = Modifier.background(MaterialTheme.colorScheme.onBackground),
    )
}