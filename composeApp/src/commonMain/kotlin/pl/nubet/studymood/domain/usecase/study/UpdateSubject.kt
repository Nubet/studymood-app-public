package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.SubjectRepository

class UpdateSubject(private val repository: SubjectRepository) {
    suspend operator fun invoke(
        subjectId: String,
        name: String? = null,
        emoji: String? = null,
        color: String? = null,
        weeklyGoalMinutes: Int? = null,
    ): Result<Unit> {
        return try {
            repository.update(subjectId, name, emoji, color, weeklyGoalMinutes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
