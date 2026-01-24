package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.SubjectRepository

class DeleteSubject(private val repository: SubjectRepository) {
    suspend operator fun invoke(subjectId: String): Result<Unit> {
        return try {
            repository.delete(subjectId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
