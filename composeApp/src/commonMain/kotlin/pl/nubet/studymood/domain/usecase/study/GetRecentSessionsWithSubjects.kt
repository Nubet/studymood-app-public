package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.domain.model.StudySession

data class StudySessionWithSubject(
    val session: StudySession,
    val subjectName: String,
    val subjectEmoji: String?,
)

class GetRecentSessionsWithSubjects(
    private val getRecentSessions: GetRecentSessions,
    private val getSubjects: GetSubjects,
) {
    suspend operator fun invoke(limit: Int = 10): List<StudySessionWithSubject> {
        val sessions = getRecentSessions(limit)
        val subjects = getSubjects()

        val subjectMap = subjects.associateBy { it.id }

        return sessions.mapNotNull { session ->
            val subject = subjectMap[session.subjectId]
            subject?.let {
                StudySessionWithSubject(
                    session = session,
                    subjectName = it.name,
                    subjectEmoji = it.emoji,
                )
            }
        }
    }
}
