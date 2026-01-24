package pl.nubet.studymood.ui.screens.quotes.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun QuotesHelpModal(isVisible: Boolean, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = isVisible,
        enter =
            fadeIn(animationSpec = tween(300)) +
                scaleIn(initialScale = 0.9f, animationSpec = tween(300)),
        exit =
            fadeOut(animationSpec = tween(300)) +
                scaleOut(targetScale = 0.9f, animationSpec = tween(300)),
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            HelpCard(onDismiss = onDismiss)
        }
    }
}

@Composable
private fun HelpCard(onDismiss: () -> Unit) {
    Column(
        modifier =
            Modifier.widthIn(max = 300.dp)
                .padding(horizontal = 24.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.2f),
                )
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                .padding(30.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Quick Guide",
            style =
                MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        GuideItem(icon = "↔", text = "Swipe Left / Right for the next quote")

        Spacer(modifier = Modifier.height(16.dp))

        GuideItem(icon = "↕", text = "Swipe Up / Down or tap the title to change category")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDismiss,
            colors =
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Got it",
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}

@Composable
private fun GuideItem(icon: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier.size(32.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = icon,
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        }

        Text(
            text = text,
            style =
                MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                ),
            modifier = Modifier.weight(1f),
        )
    }
}
