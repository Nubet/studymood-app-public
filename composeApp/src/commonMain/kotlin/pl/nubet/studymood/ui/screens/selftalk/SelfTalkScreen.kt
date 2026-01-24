package pl.nubet.studymood.ui.screens.selftalk

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.nubet.studymood.domain.model.SelfTalkSteps
import pl.nubet.studymood.presentation.selftalk.SelfTalkEvent
import pl.nubet.studymood.presentation.selftalk.SelfTalkViewModel
import pl.nubet.studymood.ui.components.interaction.pressEffect
import pl.nubet.studymood.ui.screens.selftalk.components.SegmentedProgress
import pl.nubet.studymood.ui.screens.selftalk.components.StepCard

@Composable
fun SelfTalkScreen(
    onBack: () -> Unit,
    onCheckIn: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SelfTalkViewModel = koinInject(),
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
                        viewModel.onEvent(SelfTalkEvent.NavigateBack)
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
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                CircleShape,
                            ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp),
                    )
                }

                Text(
                    text = "Self Talk",
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        ),
                )

                Spacer(Modifier.size(36.dp))
            }

            Spacer(Modifier.height(10.dp))

            SegmentedProgress(
                currentStep = state.currentStepIndex,
                totalSteps = state.totalSteps,
                progressText = state.progressText,
                modifier = Modifier.padding(vertical = 10.dp),
            )

            Spacer(Modifier.height(30.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (state.isCompleted) {
                    StepCard(
                        step =
                            pl.nubet.studymood.domain.model.SelfTalkStep(
                                icon = SelfTalkSteps.completionIcon,
                                title = SelfTalkSteps.completionTitle,
                                description = SelfTalkSteps.completionDescription,
                                buttonText = "",
                            ),
                        animationKey = "completion",
                    )
                } else {
                    StepCard(
                        step = viewModel.getCurrentStep(),
                        animationKey = state.currentStepIndex,
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = {
                        if (state.isCompleted) {
                            onBack()
                        } else if (state.currentStepIndex == state.totalSteps - 1) {
                            viewModel.onEvent(SelfTalkEvent.FinishClicked)
                        } else {
                            viewModel.onEvent(SelfTalkEvent.NextClicked)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.90f).height(60.dp).pressEffect(),
                    shape = RoundedCornerShape(99.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    val buttonText =
                        when {
                            state.isCompleted -> "Back to home"
                            else -> viewModel.getCurrentStep()?.buttonText ?: "Next"
                        }
                    Text(
                        text = buttonText,
                        style =
                            MaterialTheme.typography.labelLarge.copy(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            ),
                    )
                }

                if (state.isCompleted) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(SelfTalkEvent.CheckInClicked)
                            onCheckIn()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = "Do a check in",
                            style =
                                MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.secondary,
                                ),
                        )
                    }
                } else if (state.canGoBack) {
                    TextButton(
                        onClick = { viewModel.onEvent(SelfTalkEvent.BackClicked) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = "Go back",
                            style =
                                MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                ),
                        )
                    }
                } else {
                    TextButton(onClick = {}, enabled = false, modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Go back",
                            style =
                                MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Transparent,
                                ),
                        )
                    }
                }
            }
        }
    }
}
