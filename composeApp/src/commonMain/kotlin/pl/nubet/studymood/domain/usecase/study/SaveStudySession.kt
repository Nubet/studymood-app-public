package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.domain.model.StudySession

class SaveStudySession(private val repo: StudyRepository) {
    suspend operator fun invoke(session: StudySession) {
        repo.insert(session)
    }
}
