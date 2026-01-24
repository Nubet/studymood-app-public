package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.nubet.studymood.domain.model.Subject

@Composable
fun SubjectCard(
    subject: Subject,
    sessionCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardSize = 130.dp * 1.35f
    val emojiSize = 32.sp * 1.30f

    var scale by remember { mutableStateOf(1f) }
    var emojiRotation by remember { mutableStateOf(0f) }

    val animatedScale by
        animateFloatAsState(
            targetValue = scale,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        )

    val animatedRotation by
        animateFloatAsState(
            targetValue = emojiRotation,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        )

    val scope = rememberCoroutineScope()

    Box(
        modifier =
            modifier
                .size(width = cardSize, height = cardSize)
                .scale(animatedScale)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.04f),
                )
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color =
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp),
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            scope.launch {
                                emojiRotation = 5f
                                delay(125)
                                emojiRotation = -5f
                                delay(125)
                                emojiRotation = 0f
                            }
                            onClick()
                        },
                        onLongPress = { onLongPress() },
                        onPress = {
                            scale = 0.97f
                            tryAwaitRelease()
                            scale = 1f
                        },
                    )
                }
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = subject.emoji ?: "📘",
                fontSize = emojiSize,
                modifier = Modifier.graphicsLayer { rotationZ = animatedRotation },
            )

            Text(
                text = subject.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                textAlign = TextAlign.Center,
            )

            Text(
                text = if (sessionCount == 1) "1 session" else "$sessionCount sessions",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
