package pl.nubet.studymood.ui.screens.quotes.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.QuoteCategory

@Composable
fun CategoryDropdownMenu(
    categories: List<QuoteCategory>,
    selectedIndex: Int,
    isVisible: Boolean,
    onCategoryClick: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(200)),
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier.padding(top = 60.dp)
                        .widthIn(min = 160.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                        )
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                        .padding(8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        )
            ) {
                categories.forEachIndexed { index, category ->
                    MenuItem(
                        text = category.name,
                        isSelected = index == selectedIndex,
                        onClick = { onCategoryClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .clickable(onClick = onClick)
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    } else {
                        Color.Transparent
                    },
                    RoundedCornerShape(14.dp),
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        },
                ),
        )
    }
}
