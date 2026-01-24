package pl.nubet.studymood.domain.constants

object CheckInConstants {
    val DEFAULT_ACTIVITIES =
        listOf(
            "Working",
            "Studying",
            "Exercising",
            "Relaxing",
            "Socializing",
            "Eating",
            "Commuting",
            "Shopping",
            "Watching TV",
            "Reading",
            "Gaming",
            "Cooking",
            "Cleaning",
            "Walking",
        )

    val DEFAULT_COMPANIONS =
        listOf(
            "Alone",
            "Friends",
            "Family",
            "Partner",
            "Colleagues",
            "Classmates",
            "Pet",
            "Strangers",
        )

    val DEFAULT_LOCATIONS =
        listOf(
            "Home",
            "Work",
            "School",
            "Gym",
            "Park",
            "Restaurant",
            "Café",
            "Store",
            "Transport",
            "Outdoor",
            "Friend's place",
            "Public place",
        )

    const val MIN_ACTIVITY_LENGTH = 1

    const val MAX_CONTEXT_SUGGESTIONS = 14
}
