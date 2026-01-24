package pl.nubet.studymood.ui.screens.onboarding.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import studymood.composeapp.generated.resources.*

@Composable
fun WelcomeSlide(modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Welcome",
        headline = "Understand your days, one check-in at a time.",
        bodyText =
            "Capture how you feel and what you are focusing on instead of keeping it all in your head.",
        heroCard = {
            OnboardingHeroCard(
                cardTitle = "Your day, simplified",
                cardSubtitle = "A gentle space to log moods and focus moments without pressure.",
                illustration = painterResource(Res.drawable.undraw_check_boxes_x5fg),
                illustrationOffset = (-45).dp,
                illustrationBottomOffset = (-52).dp,
                illustrationSize = 284.dp,
                illustrationRotation = -8f,
            )
        },
        modifier = modifier,
    )
}

@Composable
fun CheckInSlide(modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Check In",
        headline = "Log your mood in seconds.",
        bodyText =
            "Tap on the mood grid, choose an emoji and add a short note if you want. No journaling required.",
        heroCard = {
            OnboardingHeroCard(
                cardTitle = "Mood grid",
                cardSubtitle = "Locate your mood across two dimensions: pleasantness and energy.",
                illustration = painterResource(Res.drawable.undraw_awesome_oikh),
                illustrationOffset = (-58).dp,
                illustrationBottomOffset = (-35).dp,
                illustrationSize = 272.dp,
                illustrationRotation = -6f,
            )
        },
        modifier = modifier,
    )
}

@Composable
fun StudySlide(modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Study",
        headline = "Track gentle focus, not hustle.",
        bodyText =
            "Pick a subject, start a session and let the app remember your effort while you stay in the flow.",
        heroCard = {
            OnboardingHeroCard(
                cardTitle = "Study sessions",
                cardSubtitle = "Over time, your study sessions form patterns worth noticing.",
                illustration = painterResource(Res.drawable.undraw_exam_prep_nmly),
                illustrationOffset = (-10).dp,
                illustrationBottomOffset = (-58).dp,
                illustrationSize = 276.dp,
                illustrationRotation = -7f,
            )
        },
        modifier = modifier,
    )
}

@Composable
fun AnalyzeSlide(modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Insights",
        headline = "See patterns in mood and focus.",
        bodyText =
            "Each check-in adds to a bigger picture. Over time, patterns emerge across emotions, energy levels, and moments of the day.",
        heroCard = {
            OnboardingHeroCard(
                cardTitle = "Mood calendar",
                cardSubtitle = "Colors and shapes summarise how each day felt.",
                illustration = painterResource(Res.drawable.undraw_pie_chart_eo9h),
                illustrationOffset = (-66).dp,
                illustrationBottomOffset = (-58).dp,
                illustrationSize = 270.dp,
                illustrationRotation = -6f,
            )
        },
        modifier = modifier,
    )
}
