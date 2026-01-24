package pl.nubet.studymood.ui.screens.quotes.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun QuotesTopBar(
    categoryTitle: String,
    isMenuOpen: Boolean,
    onBackClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    val chevronRotation by
        animateFloatAsState(targetValue = if (isMenuOpen) 180f else 0f, animationSpec = tween(300))

    Row(
        modifier = modifier.fillMaxWidth().height(44.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier =
                Modifier.size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        CircleShape,
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        CircleShape,
                    ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }

        Row(
            modifier =
                Modifier.clickable(onClick = onCategoryClick)
                    .padding(horizontal = dimens.x12, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = categoryTitle,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.01).sp,
                    ),
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (isMenuOpen) "Close menu" else "Open menu",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp).rotate(chevronRotation),
            )
        }

        IconButton(
            onClick = onHelpClick,
            modifier =
                Modifier.size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        CircleShape,
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        CircleShape,
                    ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                contentDescription = "Help",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
