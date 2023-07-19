package app.remote_bind.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import app.remote_bind.Config
import app.remote_bind.Instance
import app.remote_bind.Server
import app.remote_bind.rm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDialog(
    showMenu: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    config: Config,
) {
    AlertDialog(
        onDismissRequest = { showMenu.value = false },
    ) {
        Column {
            Button(onClick = {
                showMenu.value = false
                showDialog.value = true
            }, Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Edit, null, tint = MaterialTheme.colorScheme.background)
                Spacer(modifier = Modifier.width(12.dp))
                Text("修改")
            }
            Button(onClick = {
                val ok = when (config) {
                    is Server -> rm<Server>(config.name)
                    is Instance -> rm<Instance>(config.name)
                    else -> false
                }
                if (ok)
                    showToast("删除 ${config.name} 成功")
                showMenu.value = false
            }, Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Delete, null, tint = MaterialTheme.colorScheme.background)
                Spacer(modifier = Modifier.width(12.dp))
                Text("删除")
            }
        }
    }
}

@Composable
fun InstanceItem(
    value: Instance
) {
    val showMenu= remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    if (showMenu.value) {
        MenuDialog(showMenu, showDialog, value)
    }
    if (showDialog.value) {
        ConfigDialog(
            showDialog,
            instance = value,
            isModify = true,
        )
    }
    Spacer(modifier = Modifier.size(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    showMenu.value = true
                })
            },
    ) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center,
        ) {
            Text(text = value.name)
        }
        Divider()
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceAround,
            ) {
                Column {
                    Text(text = "Address:", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(text = value.local_address, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Column {
                    Text(text = "Server:", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(text = value.server_name, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Column {
                    Text(text = "Port:", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(text = value.remote_port.toString(), modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun ServerItem(
    value: Server
) {
    val showMenu= remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    if (showMenu.value) {
        MenuDialog(showMenu, showDialog, value)
    }
    if (showDialog.value) {
        ConfigDialog(
            showDialog,
            server = value,
            isModify = true,
        )
    }
    Spacer(modifier = Modifier.size(10.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    showMenu.value = true
                })
            },
    ) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center,
        ) {
            Text(text = value.name)
        }
        Divider()
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column() {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                ) {
                    Text(text = "Address:")
                }
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                ) {
                    Text(text = value.address, modifier = Modifier)
                }
            }
        }
    }
}