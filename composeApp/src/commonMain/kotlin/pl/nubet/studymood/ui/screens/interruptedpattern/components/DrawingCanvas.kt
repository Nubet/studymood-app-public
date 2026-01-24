package pl.nubet.studymood.ui.screens.interruptedpattern.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.domain.model.DrawingStroke
import pl.nubet.studymood.domain.model.NormalizedPoint
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternEvent
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternState

@Composable
fun DrawingCanvas(
    state: InterruptedPatternState,
    onPointerEvent: (InterruptedPatternEvent) -> Unit,
    onCanvasSizeChanged: (width: Float, height: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val patternColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    val strokeColor = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier =
            modifier
                .onSizeChanged { size ->
                    canvasSize = size
                    onCanvasSizeChanged(size.width.toFloat(), size.height.toFloat())
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val normalized = offset.toNormalized(canvasSize)
                            onPointerEvent(InterruptedPatternEvent.PointerDown(normalized))
                        },
                        onDrag = { change, _ ->
                            val normalized = change.position.toNormalized(canvasSize)
                            onPointerEvent(InterruptedPatternEvent.PointerMove(normalized))
                            change.consume()
                        },
                        onDragEnd = { onPointerEvent(InterruptedPatternEvent.PointerUp) },
                    )
                }
    ) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            drawPattern(
                pattern = state.currentPattern,
                alpha = state.patternAlpha,
                canvasSize = size,
                patternColor = patternColor,
                primaryColor = primaryColor,
            )

            drawStrokes(strokes = state.strokes, canvasSize = size, strokeColor = strokeColor)
        }
    }
}

private fun DrawScope.drawPattern(
    pattern: List<NormalizedPoint>,
    alpha: Float,
    canvasSize: Size,
    patternColor: Color,
    primaryColor: Color,
) {
    if (pattern.isEmpty() || alpha < 0.01f) return

    val points = pattern.map { it.toPixels(canvasSize) }

    if (points.size > 1) {
        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        points.drop(1).forEach { point -> path.lineTo(point.x, point.y) }

        drawPath(
            path = path,
            color = patternColor.copy(alpha = alpha * 0.5f),
            style =
                Stroke(
                    width = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f)),
                ),
        )
    }

    points.forEach { point ->
        drawCircle(
            color = primaryColor.copy(alpha = alpha * 0.9f),
            radius = 6.dp.toPx(),
            center = point,
        )
    }
}

private fun DrawScope.drawStrokes(
    strokes: List<DrawingStroke>,
    canvasSize: Size,
    strokeColor: Color,
) {
    strokes.forEach { stroke ->
        if (stroke.size < 2) return@forEach

        val path = Path()
        val first = stroke.points[0].toPixels(canvasSize)
        path.moveTo(first.x, first.y)

        stroke.points.drop(1).forEach { point ->
            val px = point.toPixels(canvasSize)
            path.lineTo(px.x, px.y)
        }

        drawPath(
            path = path,
            color = strokeColor.copy(alpha = 0.9f),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

private fun Offset.toNormalized(canvasSize: IntSize): NormalizedPoint {
    if (canvasSize.width == 0 || canvasSize.height == 0) {
        return NormalizedPoint(0f, 0f)
    }
    return NormalizedPoint(
        x = (x / canvasSize.width).coerceIn(0f, 1f),
        y = (y / canvasSize.height).coerceIn(0f, 1f),
    )
}

private fun NormalizedPoint.toPixels(canvasSize: Size): Offset {
    return Offset(x = x * canvasSize.width, y = y * canvasSize.height)
}
