package pl.nubet.studymood.ui.screens.breathing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.presentation.breathing.BreathingState

@Composable
fun BreathingCard(state: BreathingState, modifier: Modifier = Modifier) {
    val centerColor = MaterialTheme.colorScheme.primaryContainer
    val edgeColor = MaterialTheme.colorScheme.surface

    Box(
        modifier =
            modifier
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = Color.Black.copy(alpha = 0.06f),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush =
                        Brush.radialGradient(
                            colors = listOf(centerColor, edgeColor),
                            center = androidx.compose.ui.geometry.Offset(0.5f, 0f),
                        )
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Cycle ${state.currentCycle} / ${state.exerciseType.config.maxCycles}",
            style =
                MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp,
                ),
            modifier =
                Modifier.align(Alignment.TopCenter)
                    .padding(top = 24.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        RoundedCornerShape(99.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
        )

        Box(
            modifier = Modifier.fillMaxSize(0.65f).aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            BreathingRing(progress = state.sessionProgress, modifier = Modifier.fillMaxSize())

            InnerVisualizer(
                phase = state.currentPhase,
                timeLeft = state.phaseTimeLeftSeconds,
                scale = state.visualizerScale,
            )
        }
    }
}
