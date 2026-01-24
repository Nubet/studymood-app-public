package pl.nubet.studymood.ui.screens.reframe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.model.MindToolExercise

@Composable
fun SuggestionCard(
    category: MindToolCategory,
    exercise: MindToolExercise,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val depthGradient =
        Brush.linearGradient(
            colors =
                listOf(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                ),
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(0f, 350f),
        )

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 150.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize().background(depthGradient)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SigilIcon(iconRes = category.iconRes, size = 32.dp, iconSize = 18.dp)

                    Text(
                        text = "Suggested",
                        style =
                            MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                letterSpacing = 0.3.sp,
                            ),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = category.title,
                        style =
                            MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                letterSpacing = 0.3.sp,
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = exercise.title,
                        style =
                            MaterialTheme.typography.titleMedium.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 24.sp,
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = exercise.description,
                        style =
                            MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.82f),
                                lineHeight = 17.sp,
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${exercise.durationMinutes} min read",
                        style =
                            MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            ),
                    )

                    Text(
                        text = "→",
                        style =
                            MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            ),
                    )
                }
            }
        }
    }
}
