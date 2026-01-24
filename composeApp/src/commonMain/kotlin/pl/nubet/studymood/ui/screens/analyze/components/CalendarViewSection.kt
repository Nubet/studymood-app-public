package pl.nubet.studymood.ui.screens.analyze.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import pl.nubet.studymood.domain.model.MoodCheckIn
import pl.nubet.studymood.domain.model.Quadrant
import pl.nubet.studymood.presentation.analyze.DayCell
import pl.nubet.studymood.ui.theme.EmotionColors
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun CalendarViewSection(
    calendarMatrix: List<List<DayCell>>,
    selectedDate: LocalDate?,
    onDayClicked: (LocalDate) -> Unit,
    selectedDayMoods: List<MoodCheckIn>,
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x16),
    ) {
        Text(
            text = "All the emotions you felt",
            style = typography.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .shadow(2.dp, shapes.card)
                    .background(MaterialTheme.colorScheme.surface, shapes.card)
                    .padding(dimens.x16)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.x8)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                        Text(
                            text = day,
                            style =
                                typography.caption.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.5.sp,
                                ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                calendarMatrix.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        week.forEach { dayCell ->
                            CalendarDayCell(
                                dayCell = dayCell,
                                isSelected = dayCell.date == selectedDate,
                                onDayClicked = onDayClicked,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }

        if (selectedDate != null && selectedDayMoods.isNotEmpty()) {
            SelectedDaySummary(
                date = selectedDate,
                moods = selectedDayMoods,
                onDismiss = { onDayClicked(selectedDate) },
            )
        }
    }
}

@Composable
private fun CalendarDayCell(
    dayCell: DayCell,
    isSelected: Boolean,
    onDayClicked: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .padding(2.dp)
                .then(
                    if (dayCell.date != null) {
                        Modifier.clip(RoundedCornerShape(8.dp))
                            .clickable { onDayClicked(dayCell.date) }
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else if (dayCell.isToday)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = if (dayCell.isToday) 1.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp),
                            )
                    } else Modifier
                )
                .padding(dimens.x4),
        contentAlignment = Alignment.TopStart,
    ) {
        if (dayCell.date != null) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = dayCell.date.day.toString(),
                    style =
                        LocalAppTypography.current.caption.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                for (mood in dayCell.moods.take(3)) {
                    val quadrantNum =
                        when (mood.quadrant) {
                            Quadrant.Yellow -> 1
                            Quadrant.Red -> 2
                            Quadrant.Blue -> 3
                            Quadrant.Green -> 4
                        }

                    Box(
                        modifier =
                            Modifier.width(20.dp)
                                .height(4.dp)
                                .background(
                                    EmotionColors.forQuadrant(quadrantNum).copy(alpha = 0.7f),
                                    RoundedCornerShape(2.dp),
                                )
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedDaySummary(date: LocalDate, moods: List<MoodCheckIn>, onDismiss: () -> Unit) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .shadow(1.dp, shapes.card)
                .background(MaterialTheme.colorScheme.surfaceVariant, shapes.card)
                .padding(dimens.x16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimens.x8),
            ) {
                Text(
                    text = "${date.month.name} ${date.day}, ${date.year}",
                    style = typography.body1.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "${moods.size} check-in${if (moods.size != 1) "s" else ""}",
                    style = typography.body2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )

                Text(
                    text = moods.joinToString(", ") { it.emotion },
                    style = typography.body2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier =
                    Modifier.size(32.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss)
                        .padding(dimens.x8),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✕",
                    style = typography.body1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
