package pl.nubet.studymood.data.repository

import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.domain.model.Subject

class SubjectRepositoryImpl(private val database: StudyMoodDatabase) : SubjectRepository {
    private val queries
        get() = database.subjectsQueries

    override suspend fun list(): List<Subject> =
        queries.listSubjects().executeAsList().map { row ->
            Subject(
                id = row.id,
                name = row.name,
                emoji = row.emoji,
                color = row.color,
                weeklyGoalMinutes = row.weekly_goal_minutes?.toInt(),
            )
        }

    override suspend fun insert(subject: Subject) {
        queries.insertSubject(
            id = subject.id,
            name = subject.name,
            emoji = subject.emoji,
            color = subject.color,
            weekly_goal_minutes = subject.weeklyGoalMinutes?.toLong(),
        )
    }

    override suspend fun delete(id: String) {
        queries.deleteSubject(id)
    }

    override suspend fun update(
        id: String,
        name: String?,
        emoji: String?,
        color: String?,
        weeklyGoalMinutes: Int?,
    ) {
        val current = getById(id) ?: return
        queries.updateSubject(
            id = id,
            name = name ?: current.name,
            emoji = emoji ?: current.emoji,
            color = color ?: current.color,
            weekly_goal_minutes = (weeklyGoalMinutes ?: current.weeklyGoalMinutes)?.toLong(),
        )
    }

    override suspend fun getById(id: String): Subject? {
        return queries.getSubjectById(id).executeAsOneOrNull()?.let { row ->
            Subject(
                id = row.id,
                name = row.name,
                emoji = row.emoji,
                color = row.color,
                weeklyGoalMinutes = row.weekly_goal_minutes?.toInt(),
            )
        }
    }

    override suspend fun deleteAll() {
        queries.deleteAll()
    }
}
