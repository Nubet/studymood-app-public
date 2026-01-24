package pl.nubet.studymood.ui.screens.disappearingcenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.DisappearingCenterState
import pl.nubet.studymood.domain.model.NormalizedPoint
import pl.nubet.studymood.presentation.disappearingcenter.DisappearingCenterEvent

@Composable
fun CenterDrawingCard(
    state: DisappearingCenterState,
    onEvent: (DisappearingCenterEvent) -> Unit,
    onCanvasSizeChanged: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .height(400.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(26.dp),
                    )
                    .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "DRAWING EXERCISE",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.16.sp,
                    )
                    Text(
                        text = state.phaseLabel,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(20.dp),
                            )
                ) {
                    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

                    CenterCanvas(
                        centerPoint = state.centerPoint,
                        centerAlpha = state.centerAlpha,
                        centerRadius = state.centerRadius,
                        strokes = state.strokes,
                        currentStroke = emptyList(),
                        modifier =
                            Modifier.fillMaxSize()
                                .onSizeChanged { size ->
                                    canvasSize = size
                                    onCanvasSizeChanged(size.width.toFloat(), size.height.toFloat())
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            val normalized = offset.toNormalized(canvasSize)
                                            onEvent(DisappearingCenterEvent.PointerDown(normalized))
                                        },
                                        onDrag = { change, _ ->
                                            val normalized =
                                                change.position.toNormalized(canvasSize)
                                            onEvent(DisappearingCenterEvent.PointerMove(normalized))
                                            change.consume()
                                        },
                                        onDragEnd = { onEvent(DisappearingCenterEvent.PointerUp) },
                                    )
                                },
                    )

                    if (state.canvasHint.isNotEmpty()) {
                        Box(
                            modifier =
                                Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
                        ) {
                            Text(
                                text = state.canvasHint,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                if (state.hasViolatedCenter) {
                    Text(
                        text = "It's okay to touch the center. Keep going.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
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
