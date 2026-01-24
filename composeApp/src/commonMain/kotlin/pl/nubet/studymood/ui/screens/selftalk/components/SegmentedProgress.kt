package pl.nubet.studymood.ui.screens.selftalk.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SegmentedProgress(
    currentStep: Int,
    totalSteps: Int,
    progressText: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(totalSteps) { index ->
                val isActive = index <= currentStep
                val color by
                    animateColorAsState(
                        targetValue =
                            if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                        animationSpec = tween(400),
                        label = "segment_color_$index",
                    )

                Box(
                    modifier =
                        Modifier.weight(1f)
                            .height(6.dp)
                            .background(color = color, shape = RoundedCornerShape(10.dp))
                )
            }
        }

        Text(
            text = progressText,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.widthIn(min = 30.dp),
        )
    }
}
