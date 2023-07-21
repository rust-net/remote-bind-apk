package app.remote_bind.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import app.remote_bind.Config
import app.remote_bind.Instance
import app.remote_bind.Server
import app.remote_bind.addConfig
import app.remote_bind.rm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigDialog(
    showDialog: MutableState<Boolean>,
    server: Server? = null,
    instance: Instance? = null,
    onOk: (value: Config, showDialog: MutableState<Boolean>, isModify: Boolean) -> Boolean = ::addConfig,
    isModify: Boolean = false,
) {
    var name by remember { mutableStateOf(server?.name ?: instance?.name !!) }
    // server
    var address by remember { mutableStateOf(server?.address ?: "") }
    var password by remember { mutableStateOf(server?.password ?: "") }
    // instance
    var local_address by remember { mutableStateOf(instance?.local_address ?: "") }
    var server_name by remember { mutableStateOf(instance?.server_name ?: "") }
    var remote_port by remember { mutableStateOf(instance?.remote_port?.toString() ?: "") }
    val textFieldColors = TextFieldDefaults.textFieldColors(
        focusedLabelColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = MaterialTheme.colorScheme.outline,
    )
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
            Column {
                if (server != null) {
                    TextField(
                        maxLines = 1,
                        label = {
                            Text("name:")
                        },
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        colors = textFieldColors,
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    TextField(
                        maxLines = 1,
                        label = {
                            Text("address:")
                        },
                        value = address,
                        onValueChange = {
                            address = it
                        },
                        colors = textFieldColors,
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    TextField(
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation(),
                        label = {
                            Text("password:")
                        },
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        colors = textFieldColors,
                    )
                } else if (instance != null) {
                    TextField(
                        maxLines = 1,
                        label = {
                            Text("name:")
                        },
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        colors = textFieldColors,
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    TextField(
                        maxLines = 1,
                        label = {
                            Text("local_address:")
                        },
                        value = local_address,
                        onValueChange = {
                            local_address = it
                        },
                        colors = textFieldColors,
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    TextField(
                        maxLines = 1,
                        label = {
                            Text("server_name:")
                        },
                        value = server_name,
                        onValueChange = {
                            server_name = it
                        },
                        colors = textFieldColors,
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    TextField(
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        label = {
                            Text("remote_port:")
                        },
                        value = remote_port,
                        onValueChange = { input ->
                            remote_port = input.toUShortOrNull()?.toString() ?: "0"
                        },
                        colors = textFieldColors,
                    )
                }
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = {
                if (server != null) {
                    // 修改完成后，如果配置名发生了变化，则删除原来的配置
                    if (onOk(Server(name, address, password), showDialog, isModify && server.name == name)) {
                        isModify && server.name != name && rm<Server>(server.name)
                    }
                } else if (instance != null) {
                    if (onOk(Instance(name, server_name, remote_port.toUShort(), local_address), showDialog, isModify && instance.name == name)) {
                        isModify && instance.name != name && rm<Instance>(instance.name)
                    }
                }
            }, colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
            )) {
                Text("确定", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = {
                showDialog.value = false
            }, colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
            )) {
                Text("取消", color = MaterialTheme.colorScheme.primary)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true, // 允许通过按下返回键关闭Dialog
            dismissOnClickOutside = false, // 不允许点击外部区域关闭Dialog
            securePolicy = SecureFlagPolicy.SecureOn
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth(),
    )
}