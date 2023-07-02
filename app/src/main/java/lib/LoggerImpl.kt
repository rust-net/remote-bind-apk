package lib

import android.util.Log

class LoggerImpl : ILoggerImpl {
    override fun print(level: Level, tag: String, msg: String) {
        Log.println(level.value, tag, msg)
    }
}