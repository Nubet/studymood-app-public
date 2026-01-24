package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.domain.model.StudySession

class GetRecentSessions(private val repository: StudyRepository) {
    suspend operator fun invoke(limit: Int = 10): List<StudySession> {
        return repository.recent(limit)
    }
}
