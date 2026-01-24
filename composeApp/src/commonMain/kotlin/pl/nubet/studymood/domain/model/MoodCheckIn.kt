package pl.nubet.studymood.domain.model

import kotlinx.datetime.LocalDate

data class MoodCheckIn(
    val id: String,
    val date: LocalDate,
    val timestamp: Long,
    val emotion: String,
    val pleasant: Int,
    val energy: Int,
    val quadrant: Quadrant,
    val timeOfDay: TimeOfDay,
    val note: String? = null,
)
