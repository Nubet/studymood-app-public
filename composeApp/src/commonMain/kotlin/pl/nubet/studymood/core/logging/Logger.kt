package pl.nubet.studymood.core.logging

interface Logger {
    fun d(msg: String, t: Throwable? = null, tag: String? = null)

    fun i(msg: String, t: Throwable? = null, tag: String? = null)

    fun w(msg: String, t: Throwable? = null, tag: String? = null)

    fun e(msg: String, t: Throwable? = null, tag: String? = null)
}

object Log : Logger {
    private const val isDebug: Boolean = true

    override fun d(msg: String, t: Throwable?, tag: String?) {
        if (!isDebug) return
        printLine("DEBUG", tag, msg, t)
    }

    override fun i(msg: String, t: Throwable?, tag: String?) {
        if (!isDebug) return
        printLine("INFO", tag, msg, t)
    }

    override fun w(msg: String, t: Throwable?, tag: String?) {
        printLine("WARN", tag, msg, t)
    }

    override fun e(msg: String, t: Throwable?, tag: String?) {
        printLine("ERROR", tag, msg, t)
    }

    private fun printLine(level: String, tag: String?, msg: String, t: Throwable?) {
        val prefix = if (tag.isNullOrBlank()) level else "$level/$tag"
        kotlin.io.println("[$prefix] $msg")
        t?.printStackTrace()
    }
}
