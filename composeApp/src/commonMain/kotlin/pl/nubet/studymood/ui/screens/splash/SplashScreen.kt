package pl.nubet.studymood.ui.screens.splash

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import studymood.composeapp.generated.resources.Res
import studymood.composeapp.generated.resources.centered_brown_logo

@Composable
fun SplashScreen(onComplete: () -> Unit, modifier: Modifier = Modifier) {
    var startAnimation by remember { mutableStateOf(false) }

    val logoScale by
        animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0.3f,
            animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
            label = "logo_scale",
        )

    val logoAlpha by
        animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 800, easing = EaseOutCubic),
            label = "logo_alpha",
        )

    val textAlpha by
        animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 600, delayMillis = 400, easing = EaseOutCubic),
            label = "text_alpha",
        )

    val textOffsetY by
        animateDpAsState(
            targetValue = if (startAnimation) 0.dp else 20.dp,
            animationSpec = tween(durationMillis = 600, delayMillis = 400, easing = EaseOutCubic),
            label = "text_offset",
        )

    val taglineAlpha by
        animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 600, delayMillis = 800, easing = EaseOutCubic),
            label = "tagline_alpha",
        )

    LaunchedEffect(Unit) {
        startAnimation = true

        delay(2500)

        onComplete()
    }

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().offset(y = (-65).dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.centered_brown_logo),
                contentDescription = "StudyMood Logo",
                modifier = Modifier.size(150.dp).offset(x = 10.dp).scale(logoScale).alpha(logoAlpha),
            )

            Spacer(Modifier.height(28.dp))

            Text(
                text = "StudyMood",
                style =
                    MaterialTheme.typography.displaySmall.copy(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                    ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(textAlpha).offset(y = textOffsetY),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Study smarter, feel better",
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.15.sp,
                    ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(taglineAlpha),
            )
        }
    }
}
