package pl.nubet.studymood.ui.screens.analyze.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.presentation.analyze.StudyInsight
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun StudyInsightSection(insight: StudyInsight) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val shapes = LocalAppShapes.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimens.x16),
    ) {
        Text(
            text = "How studying showed up in your month",
            style = typography.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .shadow(2.dp, shapes.card)
                    .background(MaterialTheme.colorScheme.surface, shapes.card)
                    .padding(dimens.x20)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(dimens.x12)) {
                Text(
                    text = "${insight.studyDays} study days · ${insight.sessions} sessions",
                    style = typography.body2.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp),
                            )
                            .padding(horizontal = dimens.x16, vertical = dimens.x12)
                ) {
                    Text(
                        text = insight.moodSummary,
                        style = typography.body2.copy(fontSize = 14.sp, lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
