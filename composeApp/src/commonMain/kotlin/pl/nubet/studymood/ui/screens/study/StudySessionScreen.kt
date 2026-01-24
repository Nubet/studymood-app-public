package pl.nubet.studymood.ui.screens.study

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.nubet.studymood.domain.model.Subject
import pl.nubet.studymood.presentation.study.SessionStatus
import pl.nubet.studymood.presentation.study.SessionUiEvent
import pl.nubet.studymood.presentation.study.SessionViewModel
import pl.nubet.studymood.ui.components.PrimaryButton
import pl.nubet.studymood.ui.components.SecondaryButton
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    subject: Subject,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: SessionViewModel =
        koinInject(parameters = { org.koin.core.parameter.parametersOf(subject) })

    val state by vm.state.collectAsState()
    val typography = LocalAppTypography.current
    val dimens = LocalDimens.current

    LaunchedEffect(state.navigateToCheckIn, state.navigateToHome) {
        if (state.navigateToCheckIn) {
            vm.onEvent(SessionUiEvent.ResetNavigation)
            onNavigateToCheckIn()
        } else if (state.navigateToHome) {
            vm.onEvent(SessionUiEvent.ResetNavigation)
            onNavigateToHome()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(dimens.x16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SubjectEmoji(
            emoji = subject.emoji ?: "📘",
            isRunning = state.sessionStatus == SessionStatus.Running,
        )

        Spacer(Modifier.height(dimens.x12))

        Text(
            text = subject.name,
            style = typography.h2.copy(fontSize = 22.sp),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(40.dp))

        Text(
            text = vm.getTimerDisplay(),
            style = typography.h1.copy(fontSize = 52.sp, fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 1.sp,
        )

        Spacer(Modifier.height(40.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            SecondaryButton(
                onClick = { vm.onEvent(SessionUiEvent.TogglePauseResume) },
                enabled = state.sessionStatus != SessionStatus.Finished,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Text(
                    when (state.sessionStatus) {
                        SessionStatus.Running -> "Pause"
                        SessionStatus.Paused -> "Resume"
                        SessionStatus.Finished -> "Paused"
                    }
                )
            }

            PrimaryButton(
                onClick = { vm.onEvent(SessionUiEvent.FinishSession) },
                enabled = state.sessionStatus != SessionStatus.Finished,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Text("Finish Session")
            }
        }
    }

    if (state.showSummarySheet) {
        SessionSummarySheet(
            subject = subject,
            durationFormatted = state.sessionDurationFormatted,
            startFormatted = state.sessionStartFormatted,
            endFormatted = state.sessionEndFormatted,
            isSaving = state.isSaving,
            onCheckIn = { vm.onEvent(SessionUiEvent.NavigateToCheckIn) },
            onSkip = { vm.onEvent(SessionUiEvent.SkipCheckIn) },
            typography = typography,
            dimens = dimens,
        )
    }
}

@Composable
private fun SubjectEmoji(emoji: String, isRunning: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isRunning) 1.06f else 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(700, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
        )

    val alpha by
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = if (isRunning) 0.9f else 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(700, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
        )

    Text(
        text = emoji,
        fontSize = 48.sp,
        modifier =
            Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionSummarySheet(
    subject: Subject,
    durationFormatted: String,
    startFormatted: String,
    endFormatted: String,
    isSaving: Boolean,
    onCheckIn: () -> Unit,
    onSkip: () -> Unit,
    typography: pl.nubet.studymood.ui.theme.AppTypography,
    dimens: pl.nubet.studymood.ui.theme.Dimens,
) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.65f),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            shadowElevation = 8.dp,
        ) {
            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Session Summary",
                    style = typography.h3.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(dimens.x8))

                Text(
                    text = "${subject.emoji ?: "📘"} ${subject.name}",
                    style =
                        typography.body1.copy(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(dimens.x8))

                SummaryRow(label = "Duration:", value = durationFormatted, typography = typography)

                Spacer(Modifier.height(4.dp))

                SummaryRow(label = "Started:", value = startFormatted, typography = typography)

                Spacer(Modifier.height(4.dp))

                SummaryRow(label = "Ended:", value = endFormatted, typography = typography)

                Spacer(Modifier.height(dimens.x8))

                Text(
                    text = if (isSaving) "Saving session..." else "Session saved",
                    style = typography.caption,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Before you continue… how did this session leave you feeling?",
                    style = typography.body2.copy(fontSize = 14.sp, lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(16.dp))

                PrimaryButton(
                    onClick = onCheckIn,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp),
                ) {
                    Text("Check in", fontSize = 16.sp)
                }

                Spacer(Modifier.height(dimens.x8))

                SecondaryButton(
                    onClick = onSkip,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp),
                ) {
                    Text("Skip for now", fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    typography: pl.nubet.studymood.ui.theme.AppTypography,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = typography.body2.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
        )
        Text(
            text = value,
            style = typography.body2,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
        )
    }
}
