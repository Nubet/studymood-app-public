package pl.nubet.studymood.domain.constants

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object StudyConstants {
    val MICROCOPY_QUOTES =
        listOf(
            "One focus at a time.",
            "Small steps still move you forward.",
            "Clarity comes from starting.",
            "Quiet progress is still progress.",
            "Keep the pace gentle.",
            "Slow focus is still focus.",
            "Today deserves your attention.",
            "Start where you are.",
            "Consistency beats intensity.",
            "Settle into the task.",
            "Simplicity helps you move.",
            "Stay with what matters.",
        )

    val DEFAULT_SUBJECT_EMOJIS =
        listOf(
            "📘",
            "📐",
            "🧬",
            "⚗️",
            "💻",
            "📚",
            "⭐",
            "🌿",
            "🎨",
            "🎵",
            "🏃",
            "📖",
            "✏️",
            "🔬",
            "🌍",
            "🧮",
        )

    fun getTimeOfDayIcon(hour: Int): String {
        return when (hour) {
            in 5..11 -> "🌅"
            in 12..17 -> "☀️"
            in 18..21 -> "🌇"
            else -> "🌙"
        }
    }

    fun getRandomMicrocopy(seed: Int = -1): String {
        val actualSeed =
            if (seed == -1) {
                (Clock.System.now().toEpochMilliseconds() / (24 * 60 * 60 * 1000)).toInt()
            } else {
                seed
            }
        return MICROCOPY_QUOTES[actualSeed % MICROCOPY_QUOTES.size]
    }
}
