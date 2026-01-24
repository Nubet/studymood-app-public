package pl.nubet.studymood.domain.model

enum class DisappearingCenterPhase {
    INTRO,
    DRAWING,
    COMPLETE,
}

data class DisappearingCenterState(
    val phase: DisappearingCenterPhase = DisappearingCenterPhase.INTRO,
    val phaseStartTime: Long = 0L,

    val mainText: String = "Draw around the center.",
    val helperText: String = "Don't touch it.",
    val phaseLabel: String = "Focus anchor",
    val canvasHint: String = "Follow the dot as it fades.",

    val centerPoint: NormalizedPoint = NormalizedPoint(0.5f, 0.5f),
    val centerAlpha: Float = 0f,
    val centerRadius: Float = 12f,

    val strokes: List<DrawingStroke> = emptyList(),
    val totalDrawnDistance: Float = 0f,

    val hasViolatedCenter: Boolean = false,
    val violationCount: Int = 0,

    val showDoneButton: Boolean = false,
    val isFinished: Boolean = false,
)
