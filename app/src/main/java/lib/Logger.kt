package lib

val log = Logger("Global", LoggerImpl())

enum class Level(val value: Int) {
    Verbose(2),
    Debug(3),
    Info(4),
    Warn(5),
    Error(6),
}

interface ILoggerImpl {
    fun print(level: Level, tag: String, msg: String)
}

/**
 * Example:
 * ```
 * import lib.log
 *
 * log.d("The value of 1+1:", 1+1)
 * log.e("Error!", RuntimeException("Unknown"))
 * ```
 */
class Logger(private val name: String, private val loggerImpl: ILoggerImpl) {
    /**
     * Default value: 3, Android: 4, cause in Android OS:
     * ```
     * stackTrace[0]: dalvik.system.VMStack.getThreadStackTrace(Native Method)
     * stackTrace[1]: java.lang.Thread.getStackTrace(Thread.java:1736)
     * not
     * stackTrace[0]: java.lang.Thread.getStackTrace(Thread.java:1736)
     * ```
     */
    private val callerIndex: Int = when (System.getProperty("java.specification.vendor")) {
        // java[.vm].specification.{vendor/name}: "The Android Project"
        "The Android Project" -> 4
        else -> 3
    }

    private fun print(level: Level, vararg _msg: Any?) {
        var msg = _msg.joinToString(separator = " ") {
            it ?.toString() ?: "[null]"
        }

        val trace: Array<StackTraceElement>?  = Thread.currentThread().stackTrace

        if (trace == null || trace.size < callerIndex + 1) {
            loggerImpl.print(level, "Logger@$name", msg)
            return
        }

        // 0: dalvik.system.VMStack.getThreadStackTrace(Native Method)
        // 1: java.lang.Thread.getStackTrace(Thread.java:1736)
        // 2: current function, but maybe current function's default function(so, we don't use default params)
        // 3: caller, maybe it's wrapper function d() or e()
        // 4: if called by d() or e() ...etc, this is real caller
        val caller = trace[callerIndex]

        with (caller) {
            msg = "$className#$methodName($fileName:$lineNumber) -> $msg"
            loggerImpl.print(level, "Logger@$name", msg)
        }
    }

    fun v(vararg _msg: Any?) {
        this.print(Level.Verbose, *_msg)
    }

    fun d(vararg _msg: Any?) {
        this.print(Level.Debug, *_msg)
    }

    fun i(vararg _msg: Any?) {
        this.print(Level.Info, *_msg)
    }

    fun w(vararg _msg: Any?) {
        this.print(Level.Warn, *_msg)
    }

    fun e(vararg _msg: Any?) {
        this.print(Level.Error, *_msg)
    }
}
