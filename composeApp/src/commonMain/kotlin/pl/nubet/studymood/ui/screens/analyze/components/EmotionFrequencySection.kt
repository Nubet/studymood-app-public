package pl.nubet.studymood.ui.screens.analyze.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.presentation.analyze.EmotionFrequency
import pl.nubet.studymood.ui.theme.EmotionColors
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun EmotionFrequencySection(emotionFrequency: List<EmotionFrequency>) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x16),
    ) {
        Text(
            text = "The emotions you felt most often",
            style = typography.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        if (emotionFrequency.isEmpty()) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .shadow(2.dp, shapes.card)
                        .background(MaterialTheme.colorScheme.surface, shapes.card)
                        .padding(dimens.x24)
            ) {
                Text(
                    text = "No emotions recorded this month",
                    style = typography.body2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.x12)) {
                val maxCount = emotionFrequency.maxOfOrNull { it.count } ?: 1

                emotionFrequency.forEach { emotion ->
                    EmotionFrequencyRow(emotion = emotion, maxCount = maxCount)
                }
            }
        }
    }
}

@Composable
private fun EmotionFrequencyRow(emotion: EmotionFrequency, maxCount: Int) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    val quadrantNum =
        when (emotion.quadrant) {
            Quadrant.Yellow -> 1
            Quadrant.Red -> 2
            Quadrant.Blue -> 3
            Quadrant.Green -> 4
            else -> 1
        }

    val color = EmotionColors.forQuadrant(quadrantNum)
    val fillRatio = max(0.25f, emotion.count.toFloat() / maxCount.toFloat())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimens.x12),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(6.dp)))

            Text(
                text = emotion.emotion,
                style = typography.body2,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Box(
            modifier =
                Modifier.width(120.dp)
                    .height(28.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
        ) {
            Box(
                modifier =
                    Modifier.fillMaxHeight()
                        .fillMaxWidth(fillRatio)
                        .background(color.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            )

            Text(
                text = emotion.count.toString(),
                style = typography.caption.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
