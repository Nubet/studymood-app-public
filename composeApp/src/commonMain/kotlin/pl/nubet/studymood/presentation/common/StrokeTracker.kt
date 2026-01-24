package pl.nubet.studymood.presentation.common

import kotlin.math.sqrt
import pl.nubet.studymood.domain.model.DrawingStroke
import pl.nubet.studymood.domain.model.NormalizedPoint

class StrokeTracker {
    private val currentStroke = mutableListOf<NormalizedPoint>()
    private var lastPointerPos: NormalizedPoint? = null

    private var canvasWidth = 1f
    private var canvasHeight = 1f

    var totalDistance: Float = 0f
        private set

    val strokes: List<DrawingStroke>
        get() = _strokes

    private var _strokes: List<DrawingStroke> = emptyList()

    fun updateCanvasSize(width: Float, height: Float) {
        canvasWidth = width
        canvasHeight = height
    }

    fun getCanvasSize(): Pair<Float, Float> {
        return Pair(canvasWidth, canvasHeight)
    }

    fun pointerDown(point: NormalizedPoint) {
        currentStroke.clear()
        currentStroke.add(point)
        lastPointerPos = point
    }

    fun pointerMove(point: NormalizedPoint) {
        if (currentStroke.isEmpty()) return

        val last = lastPointerPos ?: return
        val distance = calculateDistance(last, point)

        currentStroke.add(point)
        lastPointerPos = point
        totalDistance += distance
    }

    fun pointerUp(): Boolean {
        if (currentStroke.size > 1) {
            _strokes = _strokes + DrawingStroke(currentStroke.toList())
        }
        currentStroke.clear()
        lastPointerPos = null
        return _strokes.isNotEmpty()
    }

    fun reset() {
        currentStroke.clear()
        lastPointerPos = null
        totalDistance = 0f
        _strokes = emptyList()
    }

    private fun calculateDistance(p1: NormalizedPoint, p2: NormalizedPoint): Float {
        val dx = (p2.x - p1.x) * canvasWidth
        val dy = (p2.y - p1.y) * canvasHeight
        return sqrt(dx * dx + dy * dy)
    }
}
