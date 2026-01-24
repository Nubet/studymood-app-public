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
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.domain.model.TimeOfDay
import pl.nubet.studymood.presentation.analyze.TimeOfDayBucket
import pl.nubet.studymood.ui.theme.EmotionColors
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun TimeOfDaySection(timeOfDayDistribution: Map<TimeOfDay, TimeOfDayBucket>) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x16),
    ) {
        Text(
            text = "Your mood during the day",
            style = typography.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TimeOfDay.values().forEach { timeOfDay ->
                TimeOfDayColumn(
                    timeOfDay = timeOfDay,
                    bucket = timeOfDayDistribution[timeOfDay],
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun TimeOfDayColumn(
    timeOfDay: TimeOfDay,
    bucket: TimeOfDayBucket?,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    val label =
        when (timeOfDay) {
            TimeOfDay.Morning -> "Morning"
            TimeOfDay.Afternoon -> "Afternoon"
            TimeOfDay.Evening -> "Evening"
            TimeOfDay.LateNight -> "Night"
        }

    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.x8),
    ) {
        Box(
            modifier =
                Modifier.width(60.dp)
                    .height(130.dp)
                    .shadow(2.dp, RoundedCornerShape(18.dp))
                    .background(
                        if (bucket == null || bucket.total == 0)
                            MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(18.dp),
                    )
        ) {
            if (bucket != null && bucket.total > 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    val sortedQuadrants =
                        listOf(Quadrant.Yellow, Quadrant.Green, Quadrant.Blue, Quadrant.Red)

                    sortedQuadrants.forEach { quadrant ->
                        val count = bucket.quadrantCounts[quadrant] ?: 0
                        if (count > 0) {
                            val heightFraction = count.toFloat() / bucket.total.toFloat()

                            val quadrantNum =
                                when (quadrant) {
                                    Quadrant.Yellow -> 1
                                    Quadrant.Red -> 2
                                    Quadrant.Blue -> 3
                                    Quadrant.Green -> 4
                                }

                            Box(
                                modifier =
                                    Modifier.fillMaxWidth()
                                        .fillMaxHeight(heightFraction)
                                        .background(
                                            EmotionColors.forQuadrant(quadrantNum)
                                                .copy(alpha = 0.75f)
                                        )
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = label,
            style = typography.caption.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = "${bucket?.total ?: 0} check-in${if (bucket?.total != 1) "s" else ""}",
            style = typography.caption.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        )
    }
}
