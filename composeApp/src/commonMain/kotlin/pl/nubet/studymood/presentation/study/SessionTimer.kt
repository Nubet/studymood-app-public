package pl.nubet.studymood.presentation.study

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider

class SessionTimer(
    private val scope: CoroutineScope,
    private val clockProvider: ClockProvider,
    private val onTick: (Long) -> Unit,
) {
    private var tickJob: Job? = null
    private var elapsedSeconds: Long = 0

    fun start() {
        stop()
        elapsedSeconds = 0
        tickJob =
            scope.launch {
                while (true) {
                    delay(1000)
                    elapsedSeconds += 1
                    onTick(elapsedSeconds)
                }
            }
    }

    fun pause() {
        tickJob?.cancel()
        tickJob = null
    }

    fun resume() {
        if (tickJob != null) return
        tickJob =
            scope.launch {
                while (true) {
                    delay(1000)
                    elapsedSeconds += 1
                    onTick(elapsedSeconds)
                }
            }
    }

    fun stop() {
        tickJob?.cancel()
        tickJob = null
    }

    fun reset() {
        stop()
        elapsedSeconds = 0
        onTick(elapsedSeconds)
    }

    fun setElapsed(seconds: Long) {
        elapsedSeconds = seconds
    }

    fun nowMillis(): Long = clockProvider.nowMillis()

    fun formatDuration(seconds: Long): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return if (mins < 60) {
            "${mins}m ${secs}s"
        } else {
            val hours = mins / 60
            val remainingMins = mins % 60
            "${hours}h ${remainingMins}m"
        }
    }

    fun formatTime(timestampMillis: Long): String {
        val totalMinutes = (timestampMillis / (1000 * 60)) % (24 * 60)
        val hours = (totalMinutes / 60).toInt()
        val minutes = (totalMinutes % 60).toInt()
        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
    }

    fun formatTimerDisplay(seconds: Long): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return if (mins < 60) {
            "${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
        } else {
            val hours = mins / 60
            val remainingMins = mins % 60
            "${hours.toString().padStart(2, '0')}:${remainingMins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
        }
    }
}
