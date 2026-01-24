package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.model.Subject

class AddSubject(private val subjectRepository: SubjectRepository) {
    suspend operator fun invoke(
        name: String,
        emoji: String?,
        color: String?,
        weeklyGoalMinutes: Int?,
    ): Result<Subject> {
        val trimmed = name.trim()
        if (trimmed.isEmpty() || trimmed.length !in 1..30)
            return Result.failure(IllegalArgumentException("Name must be 1–30 characters"))
        val subject =
            Subject(
                id = generateId(trimmed),
                name = trimmed,
                emoji = emoji,
                color = color,
                weeklyGoalMinutes = weeklyGoalMinutes,
            )
        subjectRepository.insert(subject)
        return Result.success(subject)
    }

    private fun generateId(seed: String) =
        "sub_${seed.lowercase().replace(" ", "_")}_${kotlin.random.Random.nextInt(0, 9999)}"
}
