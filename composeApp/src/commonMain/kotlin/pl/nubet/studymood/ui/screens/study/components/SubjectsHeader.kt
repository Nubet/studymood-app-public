package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.ui.components.interaction.pressEffect

@Composable
fun SubjectsHeader(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier.size(6.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                            shape = CircleShape,
                        )
            )

            Text(
                text = "SUBJECTS",
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.4.sp,
                    ),
            )
        }

        Box(
            modifier =
                Modifier.size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onAddClick)
                    .pressEffect(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "+",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
