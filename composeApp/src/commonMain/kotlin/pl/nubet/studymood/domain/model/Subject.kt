package pl.nubet.studymood.domain.model

data class Subject(
    val id: String,
    val name: String,
    val emoji: String? = null,
    val color: String? = null,
    val weeklyGoalMinutes: Int? = null,
)
