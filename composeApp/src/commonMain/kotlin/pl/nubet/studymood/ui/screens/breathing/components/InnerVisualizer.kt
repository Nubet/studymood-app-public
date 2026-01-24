package pl.nubet.studymood.ui.screens.breathing.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.presentation.breathing.BreathingPhase

@Composable
fun InnerVisualizer(
    phase: BreathingPhase,
    timeLeft: Int,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val animatedScale by
        animateFloatAsState(
            targetValue = scale,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing),
            label = "visualizer_scale",
        )

    Box(
        modifier =
            modifier
                .fillMaxSize(0.8f)
                .scale(animatedScale)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text =
                    when (phase) {
                        BreathingPhase.Ready -> "READY"
                        BreathingPhase.Inhale -> "INHALE"
                        BreathingPhase.Hold -> "HOLD"
                        BreathingPhase.Exhale -> "EXHALE"
                    },
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    ),
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (phase == BreathingPhase.Ready) "0" else timeLeft.toString(),
                style =
                    MaterialTheme.typography.displayMedium.copy(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        }
    }
}
