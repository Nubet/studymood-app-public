package pl.nubet.studymood.domain.model

enum class TimeOfDay {
    Morning,
    Afternoon,
    Evening,
    LateNight,
}

fun getTimeOfDayFromHour(hour: Int): TimeOfDay {
    return when (hour) {
        in 5..11 -> TimeOfDay.Morning
        in 12..17 -> TimeOfDay.Afternoon
        in 18..21 -> TimeOfDay.Evening
        else -> TimeOfDay.LateNight
    }
}
