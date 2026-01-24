package pl.nubet.studymood.data.repository

import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.domain.model.StudySession

class StudyRepositoryImpl(private val database: StudyMoodDatabase) : StudyRepository {
    private val queries
        get() = database.study_sessionsQueries

    override suspend fun insert(session: StudySession) {
        queries.insertSession(
            id = session.id,
            subject_id = session.subjectId,
            start_time = session.startTime,
            end_time = session.endTime,
            duration_minutes = session.durationMinutes.toLong(),
            note = session.note,
            pleasant = session.pleasant?.toLong(),
            energy = session.energy?.toLong(),
        )
    }

    override suspend fun recent(limit: Int): List<StudySession> =
        queries.recentSessions(limit.toLong()).executeAsList().map { row ->
            StudySession(
                id = row.id,
                subjectId = row.subject_id,
                startTime = row.start_time,
                endTime = row.end_time,
                durationMinutes = row.duration_minutes.toInt(),
                note = row.note,
                pleasant = row.pleasant?.toInt(),
                energy = row.energy?.toInt(),
            )
        }

    override suspend fun getSessionCountForSubject(subjectId: String): Int {
        return queries.countSessionsForSubject(subjectId).executeAsOne().toInt()
    }

    override suspend fun deleteAll() {
        queries.deleteAll()
    }
}
