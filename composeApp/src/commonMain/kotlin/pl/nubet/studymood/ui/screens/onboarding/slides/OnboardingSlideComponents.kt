package pl.nubet.studymood.ui.screens.onboarding.slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun OnboardingHeroCard(
    cardTitle: String,
    cardSubtitle: String,
    illustration: Painter,
    illustrationOffset: Dp = (-56).dp,
    illustrationBottomOffset: Dp = (-46).dp,
    illustrationSize: Dp = 260.dp,
    illustrationRotation: Float = -6f,
    contentScale: ContentScale = ContentScale.Fit,
    content: @Composable ColumnScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(220.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f),
                    spotColor = Color.Black.copy(alpha = 0.06f),
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White, MaterialTheme.colorScheme.secondaryContainer),
                        radius = 800f,
                    )
                )
    ) {
        Image(
            painter = illustration,
            contentDescription = null,
            modifier =
                Modifier.align(Alignment.BottomEnd)
                    .offset(x = illustrationOffset, y = -illustrationBottomOffset)
                    .size(illustrationSize)
                    .rotate(illustrationRotation)
                    .alpha(0.75f),
            contentScale = contentScale,
        )

        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colorStops =
                                arrayOf(
                                    0f to Color.White.copy(alpha = 0.88f),
                                    0.46f to Color.White.copy(alpha = 0.70f),
                                    1f to Color.White.copy(alpha = 0f),
                                )
                        )
                    )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimens.x20, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = cardTitle,
                    style =
                        MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cardSubtitle,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        ),
                )
            }

            content()
        }
    }
}

@Composable
fun OnboardingSlideLayout(
    tagline: String,
    headline: String,
    bodyText: String,
    heroCard: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(
                text = tagline.uppercase(),
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = headline,
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bodyText,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    ),
                modifier = Modifier.fillMaxWidth(0.9f),
            )
        }

        Box(modifier = Modifier.fillMaxWidth().padding(vertical = dimens.x12)) { heroCard() }

        Spacer(modifier = Modifier.height(dimens.x24))
    }
}
