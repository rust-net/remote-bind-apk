package app.remote_bind.ui

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import app.remote_bind.Instance
import app.remote_bind.Server
import app.remote_bind.rm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDialog(
    showMenu: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    server: Server? = null,
    instance: Instance? = null,
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
                if (server != null)
                    rm<Server>(server.name)
                else if (instance != null)
                    rm<Instance>(instance.name)
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
        MenuDialog(showMenu, showDialog, instance = value)
    }
    if (showDialog.value) {
        ConfigDialog(
            showDialog,
            instance = value,
        )
    }
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
    }
    Spacer(modifier = Modifier.size(10.dp))
}

@Composable
fun ServerItem(
    value: Server
) {
    val showMenu= remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    if (showMenu.value) {
        MenuDialog(showMenu, showDialog, server = value)
    }
    if (showDialog.value) {
        ConfigDialog(
            showDialog,
            server = value,
        )
    }
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
    Spacer(modifier = Modifier.size(10.dp))
}