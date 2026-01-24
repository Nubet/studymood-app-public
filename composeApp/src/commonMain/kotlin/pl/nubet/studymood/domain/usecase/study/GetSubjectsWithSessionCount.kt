package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.model.SubjectWithSessionCount

class GetSubjectsWithSessionCount(
    private val subjectRepository: SubjectRepository,
    private val studyRepository: StudyRepository,
) {
    suspend operator fun invoke(): List<SubjectWithSessionCount> {
        val subjects = subjectRepository.list()
        return subjects.map { subject ->
            val sessionCount = studyRepository.getSessionCountForSubject(subject.id)
            SubjectWithSessionCount(subject = subject, sessionCount = sessionCount)
        }
    }
}
