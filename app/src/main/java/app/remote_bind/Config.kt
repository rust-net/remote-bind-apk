package app.remote_bind

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.core.content.edit
import app.remote_bind.ui.configs
import app.remote_bind.ui.showToast
import lib.log

/*
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="version">3.0</string>
    <set name="instances">
        <string>instance1</string>
    </set>
    <set name="servers">
        <string>server1</string>
    </set>
    <set name="instance1">
        <string>server_name:server1</string>
        <string>remote_port:8080</string>
        <string>local_address:127.0.0.1:5555</string>
    </set>
    <set name="server1">
        <string>address:1.1.1.1:1234</string>
        <string>password:test</string>
    </set>
</map>
 */

sealed interface Config {
    var name: String
}

data class Instance(
    override var name: String,
    var server_name: String,
    var remote_port: UShort,
    var local_address: String,
) : Config {
    companion object {
        fun default() = Instance("service", "server", 0u, "127.0.0.1:5555")
    }
}

data class Server(
    override var name: String,
    var address: String,
    var password: String,
) : Config {
    companion object {
        fun default() = Server("server", "1.1.1.1:1234", "test")
    }
}

lateinit var sharedPreferences: SharedPreferences

fun getConfigs(): Pair<List<Instance>, List<Server>> {
    if (sharedPreferences.getString("version", null).apply { log.i("config version: $this") } == null) {
        log.i("config.xml load failed")
        sharedPreferences.edit(commit = true) {
            putString("version", "1.0")
        }
    }
    val instances: List<Instance> = sharedPreferences.getStringSet("instances", setOf()).let { it ->
        log.i(it?.size)
        it?.map { name ->
            log.i("name -> $name")
            val instance = sharedPreferences.getStringSet(name, setOf())
                ?.toList()
                ?: return@map null
            val inst = Instance.default()
            inst.name = name
            instance.forEach {
                val split = it.split(":", limit = 2)
                when (split[0]) {
                    "server_name" -> inst.server_name = split[1]
                    "remote_port" -> inst.remote_port = split[1].toUShort()
                    "local_address" -> inst.local_address = split[1]
                }
            }
            log.i(inst)
            inst
        } ?: listOf<Instance>()
    }.filterNotNull().sortedBy { it.name }
    val servers: List<Server> = sharedPreferences.getStringSet("servers", setOf()).let { it ->
        log.i(it?.size)
        it?.map { name ->
            log.i("name -> $name")
            val server = sharedPreferences.getStringSet(name, setOf())
                ?.toList()
                ?: return@map null
            val serv = Server.default()
            serv.name = name
            server.forEach {
                val split = it.split(":", limit = 2)
                when (split[0]) {
                    "address" -> serv.address = split[1]
                    "password" -> serv.password = split[1]
                }
            }
            log.i(serv)
            serv
        } ?: listOf<Server>()
    }.filterNotNull().sortedBy { it.name }
    return Pair(instances, servers)
}

/**
 * 添加配置，如果name重复，则修改已有配置
 */
fun addConfig(value: Config, showDialog: MutableState<Boolean>, isModify: Boolean) {
    if (!isModify && sharedPreferences.getStringSet(value.name, null) != null)
        return showToast("名称不能重复!")
    when (value) {
        is Server -> {
            val servers = sharedPreferences.getStringSet("servers", setOf())!!.toMutableSet()
            sharedPreferences.edit(commit = true) {
                putStringSet(value.name, setOf(
                    "address:${value.address}",
                    "password:${value.password}",
                ))
                putStringSet("servers", servers.apply { this.add(value.name) })
            }
        }
        is Instance -> {
            val instances = sharedPreferences.getStringSet("instances", setOf())!!.toMutableSet()
            sharedPreferences.edit(commit = true) {
                putStringSet(value.name, setOf(
                    "server_name:${value.server_name}",
                    "remote_port:${value.remote_port}",
                    "local_address:${value.local_address}",
                ))
                putStringSet("instances", instances.apply { this.add(value.name) })
            }
        }
    }
    configs.value = getConfigs()
    showDialog.value = false
}

inline fun <reified T: Config> rm(name: String): Boolean {
    val type = when (T::class) {
        Server::class -> "servers"
        Instance::class -> "instances"
        else -> return false
    }
    val names = sharedPreferences.getStringSet(type, setOf())!!.toMutableSet()
    sharedPreferences.edit(commit = true) {
        if (names.remove(name)) {
            putStringSet(type, names).remove(name)
        }
    }
    configs.value = getConfigs()
    return true
}