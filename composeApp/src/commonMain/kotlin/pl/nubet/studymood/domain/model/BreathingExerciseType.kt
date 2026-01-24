package pl.nubet.studymood.domain.model

data class BreathingExerciseConfig(
    val inhaleMs: Long,
    val hold1Ms: Long,
    val exhaleMs: Long,
    val hold2Ms: Long,
    val maxCycles: Int,
) {
    val cycleDurationMs: Long = inhaleMs + hold1Ms + exhaleMs + hold2Ms
    val totalDurationMs: Long = cycleDurationMs * maxCycles
    val totalMinutes: Int = ((totalDurationMs / 60000.0) + 0.5).toInt()
}

enum class BreathingExerciseType(
    val id: String,
    val title: String,
    val subtitle: String,
    val config: BreathingExerciseConfig,
) {
    BOX_BREATHING(
        id = "box_breathing",
        title = "2 Minute Release",
        subtitle = "Box Breathing (4-4-4-4)",
        config =
            BreathingExerciseConfig(
                inhaleMs = 4000,
                hold1Ms = 4000,
                exhaleMs = 4000,
                hold2Ms = 4000,
                maxCycles = 8,
            ),
    ),
    FOUR_SEVEN_EIGHT(
        id = "478_breathing",
        title = "Deep Calm",
        subtitle = "4-7-8 Breathing",
        config =
            BreathingExerciseConfig(
                inhaleMs = 4000,
                hold1Ms = 7000,
                exhaleMs = 8000,
                hold2Ms = 0,
                maxCycles = 6,
            ),
    );

    companion object {
        fun fromId(id: String): BreathingExerciseType? {
            return values().find { it.id == id }
        }
    }
}
