package pl.nubet.studymood.presentation.disappearingcenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.min
import kotlin.math.sqrt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.domain.model.DisappearingCenterPhase
import pl.nubet.studymood.domain.model.DisappearingCenterState
import pl.nubet.studymood.domain.model.NormalizedPoint
import pl.nubet.studymood.presentation.common.StrokeTracker

class DisappearingCenterViewModel(private val clockProvider: ClockProvider) : ViewModel() {

    private val _state = MutableStateFlow(DisappearingCenterState())
    val state = _state.asStateFlow()

    private val strokeTracker = StrokeTracker()

    private val INTRO_DURATION_MS = 1000L
    private val FADE_START_DELAY_MS = 2000L
    private val FADE_DURATION_MS = 15000L
    private var fadeStartTime: Long? = null

    init {
        startIntroPhase()
    }

    fun onEvent(event: DisappearingCenterEvent) {
        when (event) {
            is DisappearingCenterEvent.PointerDown -> handlePointerDown(event.normalizedPoint)
            is DisappearingCenterEvent.PointerMove -> handlePointerMove(event.normalizedPoint)
            is DisappearingCenterEvent.PointerUp -> handlePointerUp()
            is DisappearingCenterEvent.PhaseTimerTick -> updatePhaseLogic()
            is DisappearingCenterEvent.DoneClicked -> handleDoneClick()
            is DisappearingCenterEvent.BackClicked -> {}
        }
    }

    fun updateCanvasSize(width: Float, height: Float) {
        strokeTracker.updateCanvasSize(width, height)
    }

    private fun startIntroPhase() {
        _state.update {
            it.copy(
                phase = DisappearingCenterPhase.INTRO,
                phaseStartTime = clockProvider.nowMillis(),
                centerAlpha = 0f,
                mainText = "Draw around the center.",
                helperText = "Don't touch it.",
                phaseLabel = "Focus anchor",
                canvasHint = "Follow the dot as it fades.",
            )
        }

        viewModelScope.launch {
            delay(INTRO_DURATION_MS)
            transitionToDrawing()
        }
    }

    private fun transitionToDrawing() {
        _state.update {
            it.copy(
                phase = DisappearingCenterPhase.DRAWING,
                phaseStartTime = clockProvider.nowMillis(),
                centerAlpha = 1f,
            )
        }
    }

    private fun transitionToComplete() {
        _state.update {
            it.copy(
                phase = DisappearingCenterPhase.COMPLETE,
                phaseStartTime = clockProvider.nowMillis(),
                mainText = "Notice where your attention was.",
                helperText = "The anchor helped you focus.",
                phaseLabel = "Exercise complete",
                canvasHint = "",
                showDoneButton = true,
            )
        }
    }

    private fun handlePointerDown(point: NormalizedPoint) {
        strokeTracker.pointerDown(point)

        if (fadeStartTime == null && _state.value.phase == DisappearingCenterPhase.DRAWING) {
            fadeStartTime = clockProvider.nowMillis()
        }

        checkCenterViolation(point)
    }

    private fun handlePointerMove(point: NormalizedPoint) {
        strokeTracker.pointerMove(point)

        _state.update { it.copy(totalDrawnDistance = strokeTracker.totalDistance) }

        checkCenterViolation(point)
    }

    private fun handlePointerUp() {
        val hadStroke = strokeTracker.pointerUp()
        if (hadStroke) {
            _state.update { it.copy(strokes = strokeTracker.strokes) }
        }
    }

    private fun checkCenterViolation(point: NormalizedPoint) {
        val state = _state.value
        val center = state.centerPoint

        val dx = point.x - center.x
        val dy = point.y - center.y
        val distanceNormalized = sqrt(dx * dx + dy * dy)

        val canvasSize = strokeTracker.getCanvasSize()
        val radiusNormalized = state.centerRadius / min(canvasSize.first, canvasSize.second)

        if (distanceNormalized < radiusNormalized * 1.5f) {
            _state.update {
                it.copy(hasViolatedCenter = true, violationCount = it.violationCount + 1)
            }
        }
    }

    private fun updatePhaseLogic() {
        val state = _state.value

        when (state.phase) {
            DisappearingCenterPhase.INTRO -> {
                val elapsed = clockProvider.nowMillis() - state.phaseStartTime

                val progress = (elapsed.toFloat() / INTRO_DURATION_MS).coerceIn(0f, 1f)

                _state.update { it.copy(centerAlpha = progress) }
            }

            DisappearingCenterPhase.DRAWING -> {
                val startTime = fadeStartTime
                if (startTime != null) {
                    val elapsed = clockProvider.nowMillis() - startTime

                    if (elapsed.compareTo(FADE_START_DELAY_MS) > 0) {
                        val fadeProgress =
                            (elapsed - FADE_START_DELAY_MS).toFloat() / FADE_DURATION_MS
                        val alpha = (1f - fadeProgress).coerceIn(0f, 1f)

                        _state.update { it.copy(centerAlpha = alpha) }

                        if (alpha <= 0f) {
                            transitionToComplete()
                        }
                    }
                }
            }

            DisappearingCenterPhase.COMPLETE -> {}
        }
    }

    private fun handleDoneClick() {
        val state = _state.value

        if (!state.isFinished) {
            val violationMessage =
                if (state.violationCount > 0) {
                    "You touched the center ${state.violationCount} time(s). That's okay—it's about trying."
                } else {
                    "You stayed around the center without touching it."
                }

            _state.update {
                it.copy(
                    isFinished = true,
                    mainText = "Your focus stabilized.",
                    helperText = violationMessage,
                    phaseLabel = "Focus exercise complete",
                    canvasHint = "",
                )
            }
        }
    }
}
