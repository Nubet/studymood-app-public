package pl.nubet.studymood.presentation.interruptedpattern

import pl.nubet.studymood.domain.model.DrawingStroke
import pl.nubet.studymood.domain.model.NormalizedPoint
import pl.nubet.studymood.domain.model.PatternTemplate

enum class DrawingPhase {
    INTRO,
    TRACKING,
    INTERRUPT,
    IMPERFECT,
    FADING,
    END,
}

data class InterruptedPatternState(
    val phase: DrawingPhase = DrawingPhase.INTRO,
    val phaseStartTime: Long = 0L,
    val selectedPattern: PatternTemplate? = null,
    val basePattern: List<NormalizedPoint> = emptyList(),
    val warpedPattern: List<NormalizedPoint> = emptyList(),
    val useWarpedPattern: Boolean = false,
    val patternAlpha: Float = 0f,
    val interruptTriggered: Boolean = false,
    val strokes: List<DrawingStroke> = emptyList(),
    val totalDrawnDistance: Float = 0f,
    val mainText: String = "Follow the pattern.",
    val helperText: String = "Use your finger.",
    val phaseLabel: String = "Phase 1 · Tracking",
    val canvasHint: String = "Trace the pattern. Nothing to get right.",
    val statusText: String = "",
    val showDoneButton: Boolean = false,
    val isFinished: Boolean = false,
) {
    val currentPattern: List<NormalizedPoint>
        get() = if (useWarpedPattern) warpedPattern else basePattern
}
