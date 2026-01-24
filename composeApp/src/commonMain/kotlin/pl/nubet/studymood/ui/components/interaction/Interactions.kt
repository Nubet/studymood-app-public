package pl.nubet.studymood.ui.components.interaction

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.ui.theme.LocalAppElevations
import pl.nubet.studymood.ui.theme.LocalAppShapes

fun Modifier.pressEffect(
    enabled: Boolean = true,
    scale: Float = 0.98f,
    durationMs: Int = 140,
    role: Role? = null,
    onClick: (() -> Unit)? = null,
): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val animatedScale by
        animateFloatAsState(
            targetValue = if (pressed) scale else 1f,
            animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            label = "press-scale",
        )

    var m = this.scale(animatedScale)
    if (onClick != null) {
        m =
            m.clickable(
                enabled = enabled,
                interactionSource = interaction,
                role = role,
                onClick = onClick,
            )
    }
    m
}

fun Modifier.hoverFocusShadow(base: Dp = 0.dp, hover: Dp = 0.dp, focus: Dp = 0.dp): Modifier =
    composed {
        val interactions = remember { MutableInteractionSource() }
        val elevations = LocalAppElevations.current
        val hoverResolved = if (hover == 0.dp) elevations.level1 else hover
        val focusResolved = if (focus == 0.dp) elevations.level2 else focus
        val isHovered by interactions.collectIsHoveredAsState()
        val isFocused by interactions.collectIsFocusedAsState()
        val target =
            when {
                isFocused -> focusResolved
                isHovered -> hoverResolved
                else -> base
            }
        val elevation by animateDpAsState(targetValue = target, label = "hover-focus-elevation")
        val shape = LocalAppShapes.current.card

        this.hoverable(interactionSource = interactions)
            .shadow(elevation = elevation, shape = shape, clip = false)
    }

@Composable
fun springMediumBouncy() =
    spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)

@Composable
fun chipBumpScale(selected: Boolean): Float {
    val target = if (selected) 1.04f else 1f
    val scale by
        animateFloatAsState(
            targetValue = target,
            animationSpec = springMediumBouncy(),
            label = "chip-bump",
        )
    return scale
}

fun fadeSlideIn(durationMs: Int = 160): EnterTransition =
    fadeIn() + slideInVertically(animationSpec = spring(), initialOffsetY = { it / 6 })

fun fadeSlideOut(durationMs: Int = 160): ExitTransition =
    fadeOut() + slideOutVertically(animationSpec = spring(), targetOffsetY = { it / 6 })
