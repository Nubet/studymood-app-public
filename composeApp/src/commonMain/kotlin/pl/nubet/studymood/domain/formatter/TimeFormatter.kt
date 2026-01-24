package pl.nubet.studymood.domain.formatter

object TimeFormatter {
    fun formatDuration(seconds: Int): String {
        if (seconds < 60) {
            return "${seconds}s"
        }

        val minutes = seconds / 60
        if (minutes < 60) {
            val remainingSeconds = seconds % 60
            return if (remainingSeconds > 0) {
                "${minutes}m ${remainingSeconds}s"
            } else {
                "${minutes}m"
            }
        }

        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return if (remainingMinutes > 0) {
            "${hours}h ${remainingMinutes}m"
        } else {
            "${hours}h"
        }
    }

    fun formatMinutes(minutes: Int): String {
        if (minutes < 60) {
            return "${minutes}m"
        }

        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return if (remainingMinutes > 0) {
            "${hours}h ${remainingMinutes}m"
        } else {
            "${hours}h"
        }
    }

    fun formatTimeOfDay(hour: Int): String {
        return when (hour) {
            in 0..5 -> "Late Night"
            in 6..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..20 -> "Evening"
            else -> "Night"
        }
    }

    fun getHourFromMillis(millis: Long): Int {
        return ((millis / (1000 * 60 * 60)) % 24).toInt()
    }
}
