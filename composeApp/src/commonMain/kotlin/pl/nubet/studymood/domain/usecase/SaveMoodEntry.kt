package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.domain.model.MoodEntry

class SaveMoodEntry(private val moodRepository: MoodRepository) {
    suspend operator fun invoke(entry: MoodEntry) {
        moodRepository.insert(entry)
    }
}
