package pl.nubet.studymood.domain.model

data class MoodEntry(
    val id: String,
    val timestamp: Long,
    val pleasant: Int,
    val energy: Int,
    val labels: List<String> = emptyList(),
    val triggers: List<String> = emptyList(),
    val note: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
