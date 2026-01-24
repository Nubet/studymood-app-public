package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.domain.model.MoodEntry

class GetRecentMoods(private val moodRepository: MoodRepository) {
    suspend operator fun invoke(limit: Int = 50): List<MoodEntry> {
        return moodRepository.recent(limit)
    }
}
