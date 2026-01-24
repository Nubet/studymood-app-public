package pl.nubet.studymood.core.util

import kotlin.time.Clock

interface ClockProvider {
    fun nowMillis(): Long
}

class DefaultClockProvider : ClockProvider {
    override fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()
}
