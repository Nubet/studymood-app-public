package pl.nubet.studymood.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.sin
import pl.nubet.studymood.ui.components.interaction.chipBumpScale
import pl.nubet.studymood.ui.components.interaction.hoverFocusShadow
import pl.nubet.studymood.ui.components.interaction.pressEffect
import pl.nubet.studymood.ui.theme.LocalAppElevations
import pl.nubet.studymood.ui.theme.LocalAppShapes
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val shape = LocalAppShapes.current.button
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        contentPadding = contentPadding,
        modifier = modifier.defaultMinSize(minHeight = 44.dp).pressEffect().hoverFocusShadow(),
    ) {
        content()
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val shape = LocalAppShapes.current.button
    val container = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = container,
                contentColor = contentColor,
                disabledContainerColor = container.copy(alpha = 0.6f),
                disabledContentColor = contentColor.copy(alpha = 0.6f),
            ),
        contentPadding = contentPadding,
        modifier = modifier.defaultMinSize(minHeight = 44.dp).pressEffect().hoverFocusShadow(),
    ) {
        content()
    }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    padding: Dp = LocalDimens.current.x16,
    content: @Composable () -> Unit,
) {
    val shape = LocalAppShapes.current.card
    val elev = LocalAppElevations.current.level1
    Card(
        shape = shape,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        elevation = CardDefaults.cardElevation(elev),
        modifier = modifier.hoverFocusShadow(),
    ) {
        Box(Modifier.padding(padding)) { content() }
    }
}

@Composable
fun AppChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = LocalAppShapes.current.chip
    val bg =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    val fg =
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant
    val scale = chipBumpScale(selected)
    val outlineColor =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    Box(
        modifier
            .scale(scale)
            .clip(shape)
            .background(bg)
            .border(width = 1.dp, color = outlineColor, shape = shape)
            .pressEffect(onClick = onClick)
            .defaultMinSize(minHeight = 44.dp)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = fg, style = LocalAppTypography.current.body1)
    }
}

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = LocalAppTypography.current.h2,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Start,
        modifier = modifier,
    )
}

@Composable
fun StatCapsule(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(20.dp)
    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Text(text = label, style = LocalAppTypography.current.caption)
            Text(
                text = value,
                style = LocalAppTypography.current.body1.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
fun BreatheCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    gradient: Brush? = null,
    label: String = "Check-in",
) {
    val infinite = rememberInfiniteTransition(label = "breathe")
    val pulse by
        infinite.animateFloat(
            initialValue = 0.94f,
            targetValue = 1.06f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "pulse",
        )
    val glowAlpha by
        infinite.animateFloat(
            initialValue = 0.18f,
            targetValue = 0.35f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "glow",
        )

    val circleShape = RoundedCornerShape(percent = 50)
    Box(
        modifier =
            modifier
                .size(size)
                .scale(pulse)
                .shadow(elevation = 20.dp * glowAlpha, shape = circleShape, clip = false)
                .clip(circleShape)
                .background(
                    gradient
                        ?: Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer,
                            )
                        )
                )
                .pressEffect(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            style = LocalAppTypography.current.h2,
        )
    }
}

@Composable
fun WaveText(
    text: String,
    modifier: Modifier = Modifier,
    amplitude: Dp = 3.dp,
    speedMs: Int = 1600,
    phaseShift: Float = (PI / 6f).toFloat(),
    fontSize: TextUnit = 16.sp,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val infinite = rememberInfiniteTransition(label = "wave")

    val phase by
        infinite.animateFloat(
            initialValue = 0f,
            targetValue = (2f * PI).toFloat(),
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = speedMs, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "phase",
        )

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
        text.forEachIndexed { i, ch ->
            val y = sin(phase + i * phaseShift)

            val offset = amplitude.value * y

            Box(modifier = Modifier.graphicsLayer { translationY = offset }) {
                Text(text = ch.toString(), fontSize = fontSize, color = color)
            }
        }
    }
}
