package app.remote_bind

class Bridge {
    companion object {
        init {
            System.loadLibrary("remote_bind")
        }
    }

    external fun test()
    external fun start(server: String, port: Short, password: String, local_service: String): String
    external fun stop(handler: String)
}

val bridge = Bridge()