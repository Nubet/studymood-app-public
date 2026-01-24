package pl.nubet.studymood.ui.screens.reframe.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.model.MindToolExercise

@Composable
fun ExerciseRow(
    exercise: MindToolExercise,
    category: MindToolCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = {
            Log.d("ExerciseRow clicked: ${exercise.title} (${exercise.id})", tag = "ExerciseRow")
            onClick()
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border =
            BorderStroke(
                width = 1.dp,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    },
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                SigilIcon(iconRes = category.iconRes, size = 36.dp, iconSize = 18.dp)

                Column {
                    Text(
                        text = exercise.title,
                        style =
                            MaterialTheme.typography.titleSmall.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = exercise.description,
                        style =
                            MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${exercise.durationMinutes} min",
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                        ),
                    modifier =
                        Modifier.background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                                RoundedCornerShape(50),
                            )
                            .border(
                                1.dp,
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)
                                } else {
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                },
                                RoundedCornerShape(50),
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                )

                Box(
                    modifier =
                        Modifier.size(34.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                CircleShape,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "›",
                        style =
                            MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                    )
                }
            }
        }
    }
}
