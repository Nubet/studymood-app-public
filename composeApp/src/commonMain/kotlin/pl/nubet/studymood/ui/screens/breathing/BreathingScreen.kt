package pl.nubet.studymood.ui.screens.breathing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.nubet.studymood.domain.model.BreathingExerciseType
import pl.nubet.studymood.presentation.breathing.BreathingEvent
import pl.nubet.studymood.presentation.breathing.BreathingViewModel
import pl.nubet.studymood.ui.screens.breathing.components.BreathingCard

@Composable
fun BreathingScreen(
    exerciseType: BreathingExerciseType,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BreathingViewModel =
        koinInject(parameters = { org.koin.core.parameter.parametersOf(exerciseType) }),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(BreathingEvent.BackClicked)
                        onBack()
                    },
                    modifier =
                        Modifier.size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                CircleShape,
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                CircleShape,
                            ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp),
                    )
                }

                Text(
                    text = state.exerciseType.title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                )

                Spacer(Modifier.size(36.dp))
            }

            Spacer(Modifier.weight(1f))

            Text(
                text =
                    if (state.isFinished) {
                        "Great work. You're ready."
                    } else {
                        state.exerciseType.subtitle
                    },
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    ),
                modifier = Modifier.height(20.dp),
            )

            Spacer(Modifier.height(24.dp))

            BreathingCard(state = state, modifier = Modifier.fillMaxWidth().aspectRatio(1f))

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier.size(54.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                CircleShape,
                            )
                            .clickable { viewModel.onEvent(BreathingEvent.ResetClicked) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "↺", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                Button(
                    onClick = {
                        when {
                            state.isFinished -> viewModel.onEvent(BreathingEvent.ResetClicked)
                            state.isRunning -> viewModel.onEvent(BreathingEvent.PauseClicked)
                            state.isPaused -> viewModel.onEvent(BreathingEvent.ResumeClicked)
                            else -> viewModel.onEvent(BreathingEvent.StartClicked)
                        }
                    },
                    modifier = Modifier.height(54.dp).widthIn(min = 150.dp),
                    shape = RoundedCornerShape(99.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    Text(
                        text =
                            when {
                                state.isFinished -> "Restart"
                                state.isRunning -> "Pause"
                                state.isPaused -> "Resume"
                                else -> "Start Session"
                            },
                        style =
                            MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                            ),
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}
