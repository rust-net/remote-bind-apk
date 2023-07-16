package app.remote_bind

import android.content.SharedPreferences
import lib.log
import kotlin.streams.toList

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

data class Instance(
    var name: String,
    var server_name: String,
    var remote_port: UShort,
    var local_address: String,
) {
    companion object {
        fun default() = Instance("", "", 0u, "")
    }
}

data class Server(
    var name: String,
    var address: String,
    var password: String,
) {
    companion object {
        fun default() = Server("", "", "")
    }
}

fun getConfigs(sp: SharedPreferences): Pair<List<Instance>, List<Server>> {
    log.i(sp.getString("version", "config.xml load failed"))
    val instances: List<Instance> = sp.getStringSet("instances", setOf()).let { it ->
        log.i(it?.size)
        it?.map { name ->
            log.i("name -> $name")
            val instance = sp.getStringSet(name, setOf())
                ?.toList()
                ?: return@map null
            val inst = Instance.default()
            inst.name = name
            instance.forEach {
                val split = it.split(":")
                when (split[0]) {
                    "server_name" -> inst.server_name = split[1]
                    "remote_port" -> inst.remote_port = split[1].toUShort()
                    "local_address" -> inst.local_address = split[1]
                }
            }
            log.i(inst)
            inst
        } ?: listOf<Instance>()
    }.let {
        // filter null value
        it.stream().filter { e: Instance? ->
            e != null
        }.toList() as List<Instance>
    }
    val servers: List<Server> = sp.getStringSet("servers", setOf()).let { it ->
        log.i(it?.size)
        it?.map { name ->
            log.i("name -> $name")
            val server = sp.getStringSet(name, setOf())
                ?.toList()
                ?: return@map null
            val serv = Server.default()
            serv.name = name
            server.forEach {
                val split = it.split(":")
                when (split[0]) {
                    "address" -> serv.address = split[1]
                    "password" -> serv.password = split[1]
                }
            }
            log.i(serv)
            serv
        } ?: listOf<Server>()
    }.let {
        // filter null value
        it.stream().filter { e: Server? ->
            e != null
        }.toList() as List<Server>
    }
    return Pair(instances, servers)
}