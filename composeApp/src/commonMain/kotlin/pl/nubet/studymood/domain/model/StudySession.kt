package pl.nubet.studymood.domain.model

data class StudySession(
    val id: String,
    val subjectId: String,
    val startTime: Long,
    val endTime: Long,
    val durationMinutes: Int,
    val note: String? = null,
    val pleasant: Int? = null,
    val energy: Int? = null,
)
