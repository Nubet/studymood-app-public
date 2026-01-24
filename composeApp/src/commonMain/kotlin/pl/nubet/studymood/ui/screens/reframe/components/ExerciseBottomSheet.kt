package pl.nubet.studymood.ui.screens.reframe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.model.MindToolExercise

@Composable
fun ExerciseBottomSheet(
    category: MindToolCategory,
    exercises: List<MindToolExercise>,
    bottomPadding: Dp = 0.dp,
    onStartClick: (exerciseId: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
                .zIndex(10f)
    ) {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = 650.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                    )
                    .clickable {}
                    .padding(top = 8.dp)
                    .padding(bottom = bottomPadding)
        ) {
            Box(
                modifier =
                    Modifier.width(40.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(2.dp),
                        )
                        .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                    ) {
                        SigilIcon(iconRes = category.iconRes)

                        Column {
                            Text(
                                text = category.title,
                                style =
                                    MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    ),
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Choose one exercise.",
                                style =
                                    MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier =
                            Modifier.size(36.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    CircleShape,
                                )
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
                                    CircleShape,
                                ),
                    ) {
                        Text(
                            text = "✕",
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    exercises.forEach { exercise ->
                        ExerciseRow(
                            exercise = exercise,
                            category = category,
                            isSelected = false,
                            onClick = {
                                Log.d(
                                    "Exercise clicked, starting: ${exercise.title} (${exercise.id})",
                                    tag = "ExerciseBottomSheet",
                                )
                                onStartClick(exercise.id)
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
