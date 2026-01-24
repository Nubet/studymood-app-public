package pl.nubet.studymood.ui.screens.interruptedpattern.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternEvent
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternState

@Composable
fun DrawingCard(
    state: InterruptedPatternState,
    onPointerEvent: (InterruptedPatternEvent) -> Unit,
    onCanvasSizeChanged: (width: Float, height: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(0.95f).height(320.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp, 18.dp, 16.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CardLabelRow(label = "DRAWING EXERCISE", phaseLabel = state.phaseLabel)

            Box(
                modifier =
                    Modifier.weight(1f)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(20.dp),
                        )
                        .clip(RoundedCornerShape(20.dp))
            ) {
                DrawingCanvas(
                    state = state,
                    onPointerEvent = onPointerEvent,
                    onCanvasSizeChanged = onCanvasSizeChanged,
                    modifier = Modifier.fillMaxSize(),
                )

                if (state.canvasHint.isNotEmpty()) {
                    Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)) {
                        Text(
                            text = state.canvasHint,
                            style =
                                MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                        )
                    }
                }
            }

            if (state.statusText.isNotEmpty()) {
                Text(
                    text = state.statusText,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )
            }
        }
    }
}

@Composable
private fun CardLabelRow(label: String, phaseLabel: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    letterSpacing = 0.16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )

        Text(
            text = phaseLabel,
            style =
                MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )
    }
}
