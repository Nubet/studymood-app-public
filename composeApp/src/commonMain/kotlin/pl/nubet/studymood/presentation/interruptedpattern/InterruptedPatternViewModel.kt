package pl.nubet.studymood.presentation.interruptedpattern

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.RandomProvider
import pl.nubet.studymood.domain.model.NormalizedPoint
import pl.nubet.studymood.domain.model.PatternTemplates
import pl.nubet.studymood.presentation.common.StrokeTracker
import pl.nubet.studymood.presentation.interruptedpattern.DrawingPhase.*

class InterruptedPatternViewModel(
    private val clockProvider: ClockProvider,
    private val randomProvider: RandomProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(InterruptedPatternState())
    val state = _state.asStateFlow()

    private val strokeTracker = StrokeTracker()

    init {
        selectRandomPattern()
        startIntroPhase()
    }

    fun onEvent(event: InterruptedPatternEvent) {
        when (event) {
            is InterruptedPatternEvent.PointerDown -> handlePointerDown(event.normalizedPoint)
            is InterruptedPatternEvent.PointerMove -> handlePointerMove(event.normalizedPoint)
            is InterruptedPatternEvent.PointerUp -> handlePointerUp()
            is InterruptedPatternEvent.PhaseTimerTick -> updatePhaseLogic()
            is InterruptedPatternEvent.DoneClicked -> handleDoneClick()
            is InterruptedPatternEvent.BackClicked -> {}
        }
    }

    fun updateCanvasSize(width: Float, height: Float) {
        strokeTracker.updateCanvasSize(width, height)
    }

    private fun selectRandomPattern() {
        val template = PatternTemplates.selectRandom()
        val pattern = template.generator(1f, 1f)

        _state.update {
            it.copy(
                selectedPattern = template,
                basePattern = pattern,
                warpedPattern = pattern,
                canvasHint = template.microcopy,
            )
        }
    }

    private fun startIntroPhase() {
        _state.update {
            it.copy(phase = INTRO, phaseStartTime = clockProvider.nowMillis(), patternAlpha = 0f)
        }

        viewModelScope.launch {
            delay(1000)
            transitionToTracking()
        }
    }

    private fun transitionToTracking() {
        _state.update {
            it.copy(
                phase = TRACKING,
                phaseStartTime = clockProvider.nowMillis(),
                mainText = "Follow the pattern.",
                helperText = "Use your finger.",
                phaseLabel = "Phase 1 · Tracking",
                canvasHint =
                    it.selectedPattern?.microcopy ?: "Trace the pattern. Nothing to get right.",
            )
        }
    }

    private fun triggerInterrupt() {
        _state.update {
            it.copy(
                phase = INTERRUPT,
                interruptTriggered = true,
                phaseStartTime = clockProvider.nowMillis(),
            )
        }

        viewModelScope.launch {
            delay(1500)
            transitionToImperfect()
        }
    }

    private fun transitionToImperfect() {
        _state.update {
            it.copy(
                phase = IMPERFECT,
                phaseStartTime = clockProvider.nowMillis(),
                mainText = "Keep drawing anyway.",
                helperText = "Don't fix it.",
                phaseLabel = "Phase 2 · Let it stay imperfect",
                canvasHint = "You can drift, wobble, continue. No need to correct.",
            )
        }

        viewModelScope.launch {
            delay(4000)
            transitionToFading()
        }
    }

    private fun transitionToFading() {
        _state.update {
            it.copy(
                phase = FADING,
                phaseStartTime = clockProvider.nowMillis(),
                canvasHint = "Let the pattern fade. Your line can stay.",
            )
        }
    }

    private fun transitionToEnd() {
        _state.update {
            it.copy(
                phase = END,
                phaseStartTime = clockProvider.nowMillis(),
                mainText = "You can stop.",
                helperText = "",
                phaseLabel = "Phase 3 · End",
                canvasHint = "",
                showDoneButton = true,
            )
        }
    }

    private fun warpPatternSegment(pattern: List<NormalizedPoint>): List<NormalizedPoint> {
        if (pattern.size < 3) return pattern

        val result = pattern.toMutableList()
        val len = result.size

        val angleOffset = if (randomProvider.nextBoolean()) 18f else -18f

        val rad = angleOffset * PI.toFloat() / 180f
        val pivot = result[len - 3]

        for (i in (len - 2) until len) {
            val p = result[i]
            val dx = p.x - pivot.x
            val dy = p.y - pivot.y

            result[i] =
                NormalizedPoint(
                    x = pivot.x + (dx * cos(rad) - dy * sin(rad)),
                    y = pivot.y + (dx * sin(rad) + dy * cos(rad)),
                )
        }

        return result
    }

    private fun handlePointerDown(point: NormalizedPoint) {
        strokeTracker.pointerDown(point)
    }

    private fun handlePointerMove(point: NormalizedPoint) {
        strokeTracker.pointerMove(point)

        _state.update { it.copy(totalDrawnDistance = strokeTracker.totalDistance) }

        checkInterruptTrigger()
    }

    private fun handlePointerUp() {
        val hadStroke = strokeTracker.pointerUp()
        if (hadStroke) {
            _state.update { it.copy(strokes = strokeTracker.strokes) }
        }
    }

    private fun checkInterruptTrigger() {
        val state = _state.value
        if (
            state.phase == TRACKING && !state.interruptTriggered && state.totalDrawnDistance > 200f
        ) {
            triggerInterrupt()
        }
    }

    private fun updatePhaseLogic() {
        val state = _state.value

        when (state.phase) {
            INTRO -> {
                updatePatternAlpha(targetAlpha = 1f)
            }
            TRACKING -> {
                updatePatternAlpha(targetAlpha = 1f)
            }
            INTERRUPT,
            IMPERFECT -> {
                updatePatternAlpha(targetAlpha = 1f)
            }
            FADING -> {
                updatePatternAlpha(targetAlpha = 0f)

                if (state.patternAlpha < 0.05f) {
                    transitionToEnd()
                }
            }
            END -> {}
        }
    }

    private fun updatePatternAlpha(targetAlpha: Float) {
        _state.update { state ->
            val current = state.patternAlpha
            val speed = 0.02f

            val newAlpha =
                when {
                    current < targetAlpha -> minOf(targetAlpha, current + speed)
                    current > targetAlpha -> maxOf(targetAlpha, current - speed)
                    else -> current
                }

            state.copy(patternAlpha = newAlpha)
        }
    }

    private fun handleDoneClick() {
        val state = _state.value

        if (!state.isFinished) {
            _state.update {
                it.copy(
                    isFinished = true,
                    mainText = "Notice what happened to your focus.",
                    helperText = "Nothing to save.",
                    phaseLabel = "Exercise complete",
                    canvasHint = "",
                    patternAlpha = 0f,
                )
            }
        }
    }
}
