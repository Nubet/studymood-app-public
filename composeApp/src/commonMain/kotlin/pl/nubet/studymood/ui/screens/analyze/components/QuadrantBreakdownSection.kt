package pl.nubet.studymood.ui.screens.analyze.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.ui.theme.EmotionColors
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun QuadrantBreakdownSection(quadrantCounts: Map<Quadrant, Int>, totalCheckIns: Int) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x16),
    ) {
        Text(
            text = "Your overall check-in breakdown",
            style = typography.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .shadow(2.dp, shapes.card)
                    .background(MaterialTheme.colorScheme.surface, shapes.card)
                    .padding(dimens.x24)
        ) {
            if (totalCheckIns == 0) {
                Text(
                    text = "No check-ins this month",
                    style = typography.body2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                BubbleCluster(
                    quadrantCounts = quadrantCounts,
                    totalCheckIns = totalCheckIns,
                    modifier = Modifier.height(210.dp),
                )
            }
        }

        if (totalCheckIns > 0) {
            val dominantQuadrant = quadrantCounts.maxByOrNull { it.value }?.key
            val summaryText =
                when (dominantQuadrant) {
                    Quadrant.Yellow ->
                        "Most of your moods were high energy and pleasant this month."
                    Quadrant.Green -> "You spent most time in calm, pleasant states this month."
                    Quadrant.Blue -> "Many moments were low energy and challenging this month."
                    Quadrant.Red -> "You experienced intense, unpleasant emotions this month."
                    null -> ""
                }

            Text(
                text = summaryText,
                style = typography.body2,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun BubbleCluster(
    quadrantCounts: Map<Quadrant, Int>,
    totalCheckIns: Int,
    modifier: Modifier = Modifier,
) {
    val percentages =
        quadrantCounts.mapValues { (it.value.toFloat() / totalCheckIns * 100).toInt() }

    Box(modifier = modifier.fillMaxWidth()) {
        val sortedQuadrants = percentages.entries.sortedByDescending { it.value }

        sortedQuadrants.forEachIndexed { index, (quadrant, percentage) ->
            if (percentage > 0) {
                val (offsetX, offsetY, size) =
                    when (index) {
                        0 -> Triple(0.35f, 0.4f, (60 + percentage * 0.8f).dp)
                        1 -> Triple(0.65f, 0.3f, (50 + percentage * 0.6f).dp)
                        2 -> Triple(0.2f, 0.7f, (45 + percentage * 0.5f).dp)
                        else -> Triple(0.75f, 0.65f, (40 + percentage * 0.4f).dp)
                    }

                QuadrantBubble(
                    quadrant = quadrant,
                    percentage = percentage,
                    size = size,
                    modifier =
                        Modifier.align(Alignment.TopStart)
                            .offset(x = (offsetX * 280).dp, y = (offsetY * 150).dp),
                )
            }
        }
    }
}

@Composable
private fun QuadrantBubble(
    quadrant: Quadrant,
    percentage: Int,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    val color =
        when (quadrant) {
            Quadrant.Yellow -> EmotionColors.forQuadrant(1)
            Quadrant.Red -> EmotionColors.forQuadrant(2)
            Quadrant.Blue -> EmotionColors.forQuadrant(3)
            Quadrant.Green -> EmotionColors.forQuadrant(4)
        }

    Box(
        modifier = modifier.size(size).shadow(4.dp, CircleShape).background(color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$percentage%",
            style =
                LocalAppTypography.current.body1.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
