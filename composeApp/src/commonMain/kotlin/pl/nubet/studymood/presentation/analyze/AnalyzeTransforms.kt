package pl.nubet.studymood.presentation.analyze

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import pl.nubet.studymood.domain.model.MoodCheckIn
import pl.nubet.studymood.domain.model.MoodEntry
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.domain.model.TimeOfDay
import pl.nubet.studymood.domain.model.getQuadrantFromCoordinates
import pl.nubet.studymood.domain.model.getTimeOfDayFromHour

fun convertToCheckIn(entry: MoodEntry, onError: (String, Exception) -> Unit): MoodCheckIn? {
    return try {
        val millis = entry.timestamp

        if (millis <= 0) {
            onError(
                "Invalid timestamp for mood entry ${entry.id}: $millis",
                IllegalArgumentException("Invalid timestamp"),
            )
            return null
        }

        val hour = ((millis / (1000 * 60 * 60)) % 24).toInt()
        val daysFromEpoch = (millis / (24 * 60 * 60 * 1000)).toInt()
        val date = LocalDate.fromEpochDays(daysFromEpoch)

        val quadrant = getQuadrantFromCoordinates(entry.pleasant, entry.energy)
        val timeOfDay = getTimeOfDayFromHour(hour)
        val emotion = entry.labels.firstOrNull() ?: "Unknown"

        MoodCheckIn(
            id = entry.id,
            date = date,
            timestamp = entry.timestamp,
            emotion = emotion,
            pleasant = entry.pleasant,
            energy = entry.energy,
            quadrant = quadrant,
            timeOfDay = timeOfDay,
            note = entry.note,
        )
    } catch (e: Exception) {
        onError("Error converting mood entry ${entry.id}: ${e.message}", e)
        null
    }
}

fun availableMonths(checkIns: List<MoodCheckIn>, today: LocalDate): List<YearMonth> {
    if (checkIns.isEmpty()) {
        return listOf(YearMonth(today.year, today.month.ordinal + 1))
    }

    return checkIns
        .map { YearMonth(it.date.year, it.date.month.ordinal + 1) }
        .distinct()
        .sortedWith(compareBy({ it.year }, { it.month }))
        .takeLast(6)
}

fun filterCheckInsByMonth(
    checkIns: List<MoodCheckIn>,
    selectedMonth: YearMonth,
): List<MoodCheckIn> {
    return checkIns.filter {
        it.date.year == selectedMonth.year && (it.date.month.ordinal + 1) == selectedMonth.month
    }
}

fun quadrantCounts(checkIns: List<MoodCheckIn>): Map<Quadrant, Int> {
    return checkIns.groupBy { it.quadrant }.mapValues { it.value.size }
}

fun emotionFrequency(checkIns: List<MoodCheckIn>): List<EmotionFrequency> {
    return if (checkIns.isNotEmpty()) {
        checkIns
            .groupBy { it.emotion }
            .map { (emotion, moods) ->
                EmotionFrequency(
                    emotion = emotion,
                    count = moods.size,
                    quadrant = moods.firstOrNull()?.quadrant ?: Quadrant.Yellow,
                )
            }
            .sortedByDescending { it.count }
            .take(10)
    } else {
        emptyList()
    }
}

fun timeOfDayDistribution(checkIns: List<MoodCheckIn>): Map<TimeOfDay, TimeOfDayBucket> {
    return checkIns
        .groupBy { it.timeOfDay }
        .mapValues { (_, moods) ->
            TimeOfDayBucket(
                total = moods.size,
                quadrantCounts = moods.groupBy { it.quadrant }.mapValues { it.value.size },
            )
        }
}

fun buildCalendarMatrix(
    yearMonth: YearMonth,
    checkIns: List<MoodCheckIn>,
    today: LocalDate,
    onError: (String, Exception) -> Unit,
): List<List<DayCell>> {
    return try {
        val firstDay = LocalDate(yearMonth.year, yearMonth.month, 1)
        val daysInMonth =
            when (yearMonth.month) {
                2 -> if (isLeapYear(yearMonth.year)) 29 else 28
                4,
                6,
                9,
                11 -> 30
                else -> 31
            }
        val lastDay = LocalDate(yearMonth.year, yearMonth.month, daysInMonth)

        val moodsByDate = checkIns.groupBy { it.date }

        val weeks = mutableListOf<List<DayCell>>()
        var currentWeek = mutableListOf<DayCell>()

        val firstDayOfWeek = firstDay.dayOfWeek.ordinal + 1
        repeat(firstDayOfWeek - 1) { currentWeek.add(DayCell(null, emptyList(), false)) }

        var currentDate = firstDay
        while (currentDate <= lastDay) {
            currentWeek.add(
                DayCell(
                    date = currentDate,
                    moods = moodsByDate[currentDate] ?: emptyList(),
                    isToday = currentDate == today,
                )
            )

            if (currentWeek.size == 7) {
                weeks.add(currentWeek)
                currentWeek = mutableListOf()
            }

            currentDate = currentDate.plus(DatePeriod(days = 1))
        }

        if (currentWeek.isNotEmpty()) {
            while (currentWeek.size < 7) {
                currentWeek.add(DayCell(null, emptyList(), false))
            }
            weeks.add(currentWeek)
        }

        weeks
    } catch (e: Exception) {
        onError("Error building calendar matrix: ${e.message}", e)
        listOf(List(7) { DayCell(null, emptyList(), false) })
    }
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
