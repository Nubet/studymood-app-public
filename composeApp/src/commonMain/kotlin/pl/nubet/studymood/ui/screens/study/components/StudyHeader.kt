package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudyHeader(timeOfDayIcon: String, modifier: Modifier = Modifier) {
    val titleStyle =
        MaterialTheme.typography.headlineMedium.copy(
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
        )

    val iconContainerSize = 36.dp
    val iconShape = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = "Study", style = titleStyle, color = MaterialTheme.colorScheme.onBackground)

        Box(
            modifier =
                Modifier.size(iconContainerSize)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
                        shape = iconShape,
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.10f),
                        shape = iconShape,
                    )
                    .padding(6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = timeOfDayIcon, fontSize = 20.sp, color = Color.Unspecified)
        }
    }
}
