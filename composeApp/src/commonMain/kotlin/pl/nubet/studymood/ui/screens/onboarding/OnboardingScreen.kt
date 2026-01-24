package pl.nubet.studymood.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.onboarding.OnboardingEvent
import pl.nubet.studymood.presentation.onboarding.OnboardingUiState
import pl.nubet.studymood.presentation.onboarding.OnboardingViewModel
import pl.nubet.studymood.ui.screens.onboarding.slides.*
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun OnboardingScreen(onComplete: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: OnboardingViewModel = koinInject()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            onComplete()
        }
    }

    OnboardingContent(state = state, onEvent = viewModel::onEvent, modifier = modifier)
}

@Composable
private fun OnboardingContent(
    state: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = dimens.x20, vertical = dimens.x16)
    ) {
        OnboardingTopBar(onSkip = { onEvent(OnboardingEvent.SkipToEnd) })

        Spacer(modifier = Modifier.height(dimens.x12))

        OnboardingProgressBar(progress = state.progress)

        Spacer(modifier = Modifier.height(dimens.x20))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                            slideInHorizontally { width -> width / 4 } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width / 4 } + fadeOut()
                        } else {
                            slideInHorizontally { width -> -width / 4 } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width / 4 } + fadeOut()
                        }
                        .using(SizeTransform(clip = false))
                },
                label = "slide_transition",
            ) { step ->
                when (step) {
                    0 -> WelcomeSlide()
                    1 -> CheckInSlide()
                    2 -> StudySlide()
                    3 -> AnalyzeSlide()
                    4 ->
                        NameInputSlide(
                            name = state.userName,
                            onNameChange = { onEvent(OnboardingEvent.SetName(it)) },
                        )
                    5 ->
                        AgePickerSlide(
                            age = state.userAge,
                            onAgeChange = { onEvent(OnboardingEvent.SetAge(it)) },
                        )
                    6 ->
                        FocusAreaSlide(
                            selectedArea = state.selectedFocusArea,
                            onSelectArea = { onEvent(OnboardingEvent.SelectFocusArea(it)) },
                        )
                    7 ->
                        ReminderSlide(
                            selectedTime = state.selectedReminderTime,
                            onSelectTime = { onEvent(OnboardingEvent.SelectReminderTime(it)) },
                        )
                }
            }
        }

        OnboardingBottomBar(
            showBack = !state.isFirstStep,
            buttonText = state.buttonText,
            isLoading = state.isLoading,
            onBack = { onEvent(OnboardingEvent.PreviousStep) },
            onNext = {
                if (state.isLastStep) {
                    onEvent(OnboardingEvent.Complete)
                } else {
                    onEvent(OnboardingEvent.NextStep)
                }
            },
        )

        Text(
            text = state.microFooterText,
            style =
                MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                ),
            modifier =
                Modifier.fillMaxWidth()
                    .padding(top = dimens.x8)
                    .wrapContentWidth(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun OnboardingTopBar(onSkip: () -> Unit, modifier: Modifier = Modifier) {
    val dimens = LocalDimens.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.x8),
        ) {
            Box(
                modifier =
                    Modifier.size(18.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors =
                                    listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.primary,
                                    )
                            )
                        )
            )

            Text(
                text = "StudyMood",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        }

        TextButton(onClick = onSkip) {
            Text(
                text = "Skip",
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    ),
            )
        }
    }
}

@Composable
private fun OnboardingProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by
        animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(350, easing = FastOutSlowInEasing),
            label = "progress",
        )

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f))
    ) {
        Box(
            modifier =
                Modifier.fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primary,
                                )
                        )
                    )
        )
    }
}

@Composable
private fun OnboardingBottomBar(
    showBack: Boolean,
    buttonText: String,
    isLoading: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Row(
        modifier = modifier.fillMaxWidth().padding(top = dimens.x12),
        horizontalArrangement = Arrangement.spacedBy(dimens.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = showBack) {
            TextButton(
                onClick = onBack,
                enabled = showBack,
                modifier = Modifier.alpha(if (showBack) 1f else 0f),
            ) {
                Text(
                    text = "Back",
                    style =
                        MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                )
            }
        }

        Button(
            onClick = onNext,
            enabled = !isLoading,
            modifier =
                Modifier.weight(1f)
                    .height(50.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(50),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    ),
            shape = RoundedCornerShape(50),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF432D11),
                ),
            contentPadding = PaddingValues(0.dp),
        ) {
            Box(
                modifier =
                    Modifier.fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors =
                                    listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.primary,
                                    )
                            )
                        ),
                contentAlignment = Alignment.Center,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF432D11),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = buttonText,
                        style =
                            MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                    )
                }
            }
        }
    }
}
