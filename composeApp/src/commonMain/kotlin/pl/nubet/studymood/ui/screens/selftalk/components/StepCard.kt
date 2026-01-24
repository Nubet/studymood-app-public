package pl.nubet.studymood.ui.screens.selftalk.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.SelfTalkStep

@Composable
fun StepCard(step: SelfTalkStep?, animationKey: Any, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(0.90f).height(360.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = animationKey,
            transitionSpec = {
                val easingCurve = CubicBezierEasing(0.3f, 0.0f, 0.1f, 1.0f)
                val duration = 600

                fadeIn(
                    animationSpec = tween(durationMillis = duration, easing = easingCurve)
                ) togetherWith
                    fadeOut(
                        animationSpec = tween(durationMillis = duration / 2, easing = easingCurve)
                    )
            },
            label = "StepCardAnimation",
        ) { _ ->
            if (step != null) {
                StepCardContent(step = step)
            }
        }
    }
}

@Composable
private fun StepCardContent(step: SelfTalkStep) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
                    spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(32.dp),
                )
                .padding(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.CenterVertically),
        ) {
            Text(text = step.icon, fontSize = 44.sp)

            Text(
                text = step.title,
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = step.description,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
