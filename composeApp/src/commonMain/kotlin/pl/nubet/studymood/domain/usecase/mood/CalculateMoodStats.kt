package pl.nubet.studymood.domain.usecase.mood

import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.domain.model.MoodEntry

data class MoodStats(val totalMoodsLogged: Int, val currentStreakDays: Int)

class CalculateMoodStats(
    private val getRecentMoods: pl.nubet.studymood.domain.usecase.GetRecentMoods,
    private val clockProvider: ClockProvider,
) {
    suspend operator fun invoke(limit: Int = 1000): MoodStats {
        val moods = getRecentMoods(limit)
        val total = moods.size
        val streak = calculateStreak(moods)

        return MoodStats(totalMoodsLogged = total, currentStreakDays = streak)
    }

    private fun calculateStreak(moods: List<MoodEntry>): Int {
        if (moods.isEmpty()) return 0

        val daysWithMoods =
            moods.map { it.timestamp / (24 * 60 * 60 * 1000) }.distinct().sortedDescending()

        if (daysWithMoods.isEmpty()) return 0

        val today = clockProvider.nowMillis() / (24 * 60 * 60 * 1000)

        if (daysWithMoods.first() != today && daysWithMoods.first() != today - 1) {
            return 0
        }

        var streak = 1
        for (i in 1 until daysWithMoods.size) {
            if (daysWithMoods[i] == daysWithMoods[i - 1] - 1) {
                streak++
            } else {
                break
            }
        }

        return streak
    }
}
