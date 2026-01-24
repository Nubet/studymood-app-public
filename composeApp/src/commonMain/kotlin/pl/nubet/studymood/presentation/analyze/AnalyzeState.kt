package pl.nubet.studymood.presentation.analyze

import kotlinx.datetime.LocalDate
import pl.nubet.studymood.domain.model.MoodCheckIn
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.domain.model.TimeOfDay

data class AnalyzeState(
    val availableMonths: List<YearMonth> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.current(),
    val monthCheckIns: List<MoodCheckIn> = emptyList(),
    val quadrantCounts: Map<Quadrant, Int> = emptyMap(),
    val emotionFrequency: List<EmotionFrequency> = emptyList(),
    val calendarMatrix: List<List<DayCell>> = emptyList(),
    val timeOfDayDistribution: Map<TimeOfDay, TimeOfDayBucket> = emptyMap(),
    val studyInsight: StudyInsight? = null,
    val selectedDate: LocalDate? = null,
    val selectedDayMoods: List<MoodCheckIn> = emptyList(),
    val isLoading: Boolean = false,
)

data class YearMonth(val year: Int, val month: Int) {
    fun toDisplayString(): String {
        val monthNames =
            listOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
            )
        return "${monthNames[month - 1]} $year"
    }

    companion object {
        fun current(): YearMonth {
            return YearMonth(2024, 11)
        }
    }
}

data class DayCell(val date: LocalDate?, val moods: List<MoodCheckIn>, val isToday: Boolean)

data class EmotionFrequency(val emotion: String, val count: Int, val quadrant: Quadrant)

data class TimeOfDayBucket(val total: Int, val quadrantCounts: Map<Quadrant, Int>)

data class StudyInsight(
    val studyDays: Int,
    val sessions: Int,
    val calmSessions: Int,
    val moodSummary: String,
)
