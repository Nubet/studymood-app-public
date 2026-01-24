package pl.nubet.studymood.ui.screens.reframe.components

import androidx.compose.animation.animateContentSize
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
import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun CategoryCard(category: MindToolCategory, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dimens = LocalDimens.current

    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(0.9f).animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SigilIcon(iconRes = category.iconRes)

                Text(
                    text = "${category.exerciseCount}",
                    style =
                        MaterialTheme.typography.labelMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    modifier =
                        Modifier.background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                RoundedCornerShape(50),
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
                                RoundedCornerShape(50),
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                )
            }

            Column {
                Text(
                    text = category.title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = category.subtitle,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp,
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
                    text = category.tag,
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    modifier =
                        Modifier.background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                                RoundedCornerShape(50),
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
                                RoundedCornerShape(50),
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                )

                Box(
                    modifier =
                        Modifier.size(30.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                CircleShape,
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
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
