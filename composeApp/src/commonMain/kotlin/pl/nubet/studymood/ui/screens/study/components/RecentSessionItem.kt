package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecentSessionItem(
    subjectName: String,
    subjectEmoji: String?,
    durationMinutes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!subjectEmoji.isNullOrEmpty()) {
                    Text(text = subjectEmoji, fontSize = 18.sp)
                }
                Text(
                    text = subjectName,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = formatDuration(durationMinutes),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun formatDuration(minutes: Int): String {
    return when {
        minutes < 60 -> "$minutes min"
        minutes % 60 == 0 -> "${minutes / 60}h"
        else -> "${minutes / 60}h ${minutes % 60}m"
    }
}
