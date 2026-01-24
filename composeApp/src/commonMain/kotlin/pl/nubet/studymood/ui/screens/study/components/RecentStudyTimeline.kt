package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import pl.nubet.studymood.domain.usecase.study.StudySessionWithSubject
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun RecentStudyTimeline(sessions: List<StudySessionWithSubject>, modifier: Modifier = Modifier) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    if (sessions.isEmpty()) return

    val displaySessions = sessions.take(7)

    Column(modifier = modifier) {
        Spacer(Modifier.height(dimens.x16))

        Text(
            text = "RECENT SESSIONS",
            style = typography.body1.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = dimens.x8),
        )

        CurvedBubbleTimeline(
            sessions = displaySessions,
            modifier = Modifier.fillMaxWidth().height(140.dp),
        )

        Spacer(Modifier.height(dimens.x8))

        TimelineSummary(
            displayedCount = displaySessions.size,
            totalSessions = sessions,
            style = typography.caption,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
        )
    }
}

@Composable
private fun CurvedBubbleTimeline(
    sessions: List<StudySessionWithSubject>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        GradientTrackLine(modifier = Modifier.fillMaxWidth().align(Alignment.Center))

        BubblesLayout(sessions = sessions)
    }
}

@Composable
private fun GradientTrackLine(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .height(2.dp)
                .clip(CircleShape)
                .background(
                    brush =
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f),
                                    Color.Transparent,
                                ),
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY,
                        )
                )
    )
}

@Composable
private fun BubblesLayout(sessions: List<StudySessionWithSubject>) {
    Layout(
        content = { sessions.forEach { session -> SessionBubble(sessionWithSubject = session) } },
        measurePolicy = bubblePositioningPolicy(sessions.size),
    )
}

private fun bubblePositioningPolicy(count: Int): MeasurePolicy =
    MeasurePolicy { measurables, constraints ->
        val placeables =
            measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        val width = constraints.maxWidth
        val height = constraints.maxHeight

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val position =
                    calculateBubblePosition(
                        index = index,
                        total = count,
                        containerWidth = width,
                        containerHeight = height,
                        bubbleSize = placeable.width,
                    )

                placeable.place(x = position.x.roundToInt(), y = position.y.roundToInt())
            }
        }
    }

private fun calculateBubblePosition(
    index: Int,
    total: Int,
    containerWidth: Int,
    containerHeight: Int,
    bubbleSize: Int,
): Offset {
    val progress = if (total > 1) index.toFloat() / (total - 1) else 0.5f

    val horizontalMargin =
        when {
            total <= 3 -> 0.15f
            total <= 5 -> 0.12f
            else -> 0.10f
        }

    val xProgress = horizontalMargin + (progress * (1f - 2 * horizontalMargin))
    val x = (containerWidth * xProgress) - (bubbleSize / 2f)

    val baseAmplitude =
        when {
            total <= 3 -> 20f
            total <= 5 -> 28f
            else -> 36f
        }

    val amplitudeVariation = (index % 3 - 1) * 3f
    val amplitude = baseAmplitude + amplitudeVariation

    val t = progress * PI.toFloat()
    val centerY = containerHeight / 2f
    val y = centerY + (cos(t.toDouble()).toFloat() * amplitude) - (bubbleSize / 2f)

    return Offset(x, y)
}

@Composable
private fun SessionBubble(sessionWithSubject: StudySessionWithSubject) {
    var scale by remember { mutableStateOf(1f) }
    var showDuration by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val animatedScale by
        animateFloatAsState(
            targetValue = scale,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        )

    val durationAlpha by
        animateFloatAsState(
            targetValue = if (showDuration) 1f else 0f,
            animationSpec =
                tween(
                    durationMillis = if (showDuration) 120 else 180,
                    easing = if (showDuration) FastOutSlowInEasing else LinearOutSlowInEasing,
                ),
        )

    val bubbleSize = bubbleSizeFromMinutes(sessionWithSubject.session.durationMinutes)
    val bubbleColor = subjectColorFromName(sessionWithSubject.subjectName)
    val durationText = formatDuration(sessionWithSubject.session.durationMinutes)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(bubbleSize)) {
        if (showDuration || durationAlpha > 0f) {
            Text(
                text = durationText,
                modifier = Modifier.align(Alignment.TopCenter).offset(y = (-25).dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = durationAlpha * 0.9f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Box(
            modifier =
                Modifier.fillMaxSize()
                    .scale(animatedScale)
                    .shadow(
                        elevation = 3.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.04f),
                    )
                    .clip(CircleShape)
                    .background(bubbleColor)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                scope.launch {
                                    scale = 1.08f
                                    showDuration = true
                                    delay(80)
                                    scale = 0.95f
                                    delay(80)
                                    scale = 1f
                                    delay(1140)
                                    showDuration = false
                                }
                            }
                        )
                    },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = sessionWithSubject.subjectEmoji ?: "📘",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun TimelineSummary(
    displayedCount: Int,
    totalSessions: List<StudySessionWithSubject>,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
) {
    val totalCount = totalSessions.size
    val totalMinutes = totalSessions.sumOf { it.session.durationMinutes }

    val firstDay =
        if (totalSessions.isNotEmpty()) {
            formatDayOfWeek(totalSessions.last().session.startTime)
        } else ""

    val lastDay =
        if (totalSessions.isNotEmpty()) {
            formatDayOfWeek(totalSessions.first().session.startTime)
        } else ""

    val summaryText = buildString {
        if (displayedCount < totalCount) {
            append("Showing $displayedCount of $totalCount sessions")
        } else {
            append("$totalCount ")
            append(if (totalCount == 1) "session" else "sessions")
        }
        append(" · ")
        append("$totalMinutes min total")
        if (firstDay.isNotEmpty() && lastDay.isNotEmpty() && firstDay != lastDay) {
            append(" · $firstDay–$lastDay")
        }
    }

    Text(text = summaryText, style = style, color = color)
}

private fun bubbleSizeFromMinutes(minutes: Int): androidx.compose.ui.unit.Dp {
    return when {
        minutes <= 3 -> 26.dp
        minutes <= 10 -> 32.dp
        minutes <= 20 -> 38.dp
        else -> 44.dp
    }
}

private fun subjectColorFromName(subjectName: String): Color {
    val hash = subjectName.hashCode()
    val hue = abs(hash % 360)

    return when (hue) {
        in 0..30 -> Color(0xFFFFDDB3).copy(alpha = 0.85f)
        in 31..90 -> Color(0xFFD4E0C1).copy(alpha = 0.75f)
        in 91..150 -> Color(0xFFB8D8D8).copy(alpha = 0.8f)
        in 151..210 -> Color(0xFFB8C5E0).copy(alpha = 0.8f)
        in 211..270 -> Color(0xFFD8C5E0).copy(alpha = 0.8f)
        in 271..330 -> Color(0xFFE0C5D0).copy(alpha = 0.8f)
        else -> Color(0xFFD4C4B0).copy(alpha = 0.8f)
    }
}

private fun formatDayOfWeek(timestampMillis: Long): String {
    val daysFromEpoch = (timestampMillis / (24 * 60 * 60 * 1000)).toInt()
    val date = LocalDate.fromEpochDays(daysFromEpoch)

    return when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

private fun formatDuration(minutes: Int): String {
    return when {
        minutes <= 0 -> "0min"
        minutes == 1 -> "1min"
        minutes < 60 -> "${minutes}min"
        minutes == 60 -> "1h"
        minutes < 120 -> "1h ${minutes % 60}m"
        minutes % 60 == 0 -> "${minutes / 60}h"
        else -> {
            val hours = minutes / 60
            val mins = minutes % 60
            "${hours}h ${mins}m"
        }
    }
}
