package pl.nubet.studymood.ui.screens.quotes.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.Quote

@Composable
fun QuoteCard(
    quote: Quote?,
    animationKey: Any,
    isVerticalSwipe: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(0.85f).aspectRatio(4f / 5f),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = animationKey,
            transitionSpec = {
                val slideDirection =
                    if (isVerticalSwipe) {
                        if (
                            (targetState as? Pair<*, *>)?.first as? Int ?: 0 >
                                (initialState as? Pair<*, *>)?.first as? Int ?: 0
                        ) {
                            AnimatedContentTransitionScope.SlideDirection.Up
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Down
                        }
                    } else {
                        if (
                            (targetState as? Pair<*, *>)?.second as? Int ?: 0 >
                                (initialState as? Pair<*, *>)?.second as? Int ?: 0
                        ) {
                            AnimatedContentTransitionScope.SlideDirection.Left
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Right
                        }
                    }

                val easingCurve = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
                val duration = 450

                slideIntoContainer(
                    towards = slideDirection,
                    animationSpec = tween(duration, easing = easingCurve),
                    initialOffset = { fullSize -> fullSize / 4 },
                ) +
                    fadeIn(animationSpec = tween(duration, easing = easingCurve)) +
                    scaleIn(
                        initialScale = 0.97f,
                        animationSpec = tween(duration, easing = easingCurve),
                    ) togetherWith
                    slideOutOfContainer(
                        towards = slideDirection,
                        animationSpec = tween(duration, easing = easingCurve),
                        targetOffset = { fullSize -> -fullSize / 4 },
                    ) +
                        fadeOut(animationSpec = tween(duration, easing = easingCurve)) +
                        scaleOut(
                            targetScale = 0.97f,
                            animationSpec = tween(duration, easing = easingCurve),
                        )
            },
            label = "QuoteCardAnimation",
        ) { _ ->
            QuoteCardContent(quote = quote)
        }
    }
}

@Composable
private fun QuoteCardContent(quote: Quote?) {
    val centerColor = MaterialTheme.colorScheme.primaryContainer
    val edgeColor = MaterialTheme.colorScheme.surface

    Box(
        modifier =
            Modifier.fillMaxSize()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Black.copy(alpha = 0.06f),
                    spotColor = Color.Black.copy(alpha = 0.03f),
                )
                .background(
                    brush = Brush.radialGradient(colors = listOf(centerColor, edgeColor)),
                    shape = RoundedCornerShape(32.dp),
                )
                .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "\"",
                style =
                    MaterialTheme.typography.displayLarge.copy(
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Normal,
                        lineHeight = 48.sp,
                    ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = quote?.text ?: "Loading...",
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 33.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = quote?.author?.uppercase() ?: "",
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    ),
            )
        }
    }
}
