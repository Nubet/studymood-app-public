package pl.nubet.studymood.data.repository

import pl.nubet.studymood.domain.model.Subject

interface SubjectRepository {
    suspend fun list(): List<Subject>

    suspend fun insert(subject: Subject)

    suspend fun delete(id: String)

    suspend fun update(
        id: String,
        name: String? = null,
        emoji: String? = null,
        color: String? = null,
        weeklyGoalMinutes: Int? = null,
    )

    suspend fun getById(id: String): Subject?

    suspend fun deleteAll()
}
