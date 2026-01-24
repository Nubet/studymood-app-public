package pl.nubet.studymood.ui.screens.brightspots.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pl.nubet.studymood.domain.model.BrightSpotsStep

@Composable
fun BrightSpotsStepCard(step: BrightSpotsStep?, animationKey: Any, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(0.90f).height(400.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = animationKey,
            transitionSpec = {
                val easingCurve = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
                val duration = 400

                fadeIn(
                    animationSpec = tween(durationMillis = duration, easing = easingCurve)
                ) togetherWith
                    fadeOut(
                        animationSpec = tween(durationMillis = duration / 2, easing = easingCurve)
                    )
            },
            label = "BrightSpotsStepCardAnimation",
        ) { _ ->
            if (step != null) {
                BrightSpotsStepCardContent(step = step)
            }
        }
    }
}

@Composable
private fun BrightSpotsStepCardContent(step: BrightSpotsStep) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Black.copy(alpha = 0.03f),
                    spotColor = Color.Black.copy(alpha = 0.02f),
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
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.CenterVertically),
        ) {
            Image(
                painter = painterResource(step.illustration),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )

            Text(
                text = step.title,
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 27.sp,
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
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            )
        }
    }
}
