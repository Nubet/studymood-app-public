package pl.nubet.studymood.data.repository

import pl.nubet.studymood.domain.model.StudySession

interface StudyRepository {
    suspend fun insert(session: StudySession)

    suspend fun recent(limit: Int = 50): List<StudySession>

    suspend fun getSessionCountForSubject(subjectId: String): Int

    suspend fun deleteAll()
}
