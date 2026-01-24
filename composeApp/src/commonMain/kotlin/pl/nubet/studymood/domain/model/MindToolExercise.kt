package pl.nubet.studymood.domain.model

data class MindToolExercise(
    val id: String,
    val categoryId: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val instructions: List<String> = emptyList(),
    val isGuided: Boolean = true,
    val requiresFullScreen: Boolean = false,
)
