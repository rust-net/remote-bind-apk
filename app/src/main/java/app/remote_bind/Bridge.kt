package app.remote_bind

class Bridge {
    companion object {
        init {
            System.loadLibrary("remote_bind")
        }
    }

    external fun test()
}

val bridge = Bridge()