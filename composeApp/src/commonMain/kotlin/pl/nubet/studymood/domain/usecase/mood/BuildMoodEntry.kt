package pl.nubet.studymood.domain.usecase.mood

import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.RandomProvider
import pl.nubet.studymood.domain.model.Emotion
import pl.nubet.studymood.domain.model.MoodEntry

class BuildMoodEntry(
    private val clockProvider: ClockProvider,
    private val randomProvider: RandomProvider,
) {
    operator fun invoke(
        selectedEmotion: Emotion,
        pleasant: Int,
        energy: Int,
        note: String?,
        triggers: List<String>,
    ): MoodEntry {
        val now = clockProvider.nowMillis()
        val id = generateId(now)

        val labels = buildList { add(selectedEmotion.label) }

        return MoodEntry(
            id = id,
            timestamp = now,
            pleasant = pleasant,
            energy = energy,
            labels = labels,
            triggers = triggers,
            note = note,
            createdAt = now,
            updatedAt = now,
        )
    }

    private fun generateId(timestamp: Long): String {
        val random = randomProvider.nextInt(9999)
        return "mood_${timestamp}_$random"
    }
}
