package pl.nubet.studymood.ui.screens.brightspots

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
import pl.nubet.studymood.domain.model.BrightSpotsStep
import pl.nubet.studymood.domain.model.BrightSpotsSteps
import pl.nubet.studymood.presentation.brightspots.BrightSpotsEvent
import pl.nubet.studymood.presentation.brightspots.BrightSpotsViewModel
import pl.nubet.studymood.ui.components.interaction.pressEffect
import pl.nubet.studymood.ui.screens.brightspots.components.BrightSpotsProgress
import pl.nubet.studymood.ui.screens.brightspots.components.BrightSpotsStepCard

@Composable
fun BrightSpotsScreen(
    onBack: () -> Unit,
    onCheckIn: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BrightSpotsViewModel = koinInject(),
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
                        viewModel.onEvent(BrightSpotsEvent.NavigateBack)
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
                    text = "Bright Spots",
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

            if (!state.isCompleted) {
                BrightSpotsProgress(
                    currentStep = state.currentStepIndex,
                    totalSteps = state.totalSteps,
                    progressText = state.progressText,
                    modifier = Modifier.padding(vertical = 10.dp),
                )

                Spacer(Modifier.height(30.dp))
            } else {
                Spacer(Modifier.height(60.dp))
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (state.isCompleted) {
                    BrightSpotsStepCard(
                        step =
                            BrightSpotsStep(
                                illustration = BrightSpotsSteps.completionIllustration,
                                title = BrightSpotsSteps.completionTitle,
                                description = BrightSpotsSteps.completionDescription,
                                buttonText = "",
                            ),
                        animationKey = "completion",
                    )
                } else {
                    BrightSpotsStepCard(
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
                            viewModel.onEvent(BrightSpotsEvent.FinishClicked)
                        } else {
                            viewModel.onEvent(BrightSpotsEvent.NextClicked)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.90f).height(60.dp).pressEffect(),
                    shape = RoundedCornerShape(30.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                ) {
                    val buttonText =
                        when {
                            state.isCompleted -> "Back to home"
                            else -> viewModel.getCurrentStep()?.buttonText ?: "Continue"
                        }
                    Text(
                        text = buttonText,
                        style =
                            MaterialTheme.typography.labelLarge.copy(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                            ),
                    )
                }

                if (state.isCompleted) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(BrightSpotsEvent.CheckInClicked)
                            onCheckIn()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = "Check in again",
                            style =
                                MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.8f
                                        ),
                                ),
                        )
                    }
                } else if (state.canGoBack) {
                    TextButton(
                        onClick = { viewModel.onEvent(BrightSpotsEvent.BackClicked) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = "Go back",
                            style =
                                MaterialTheme.typography.labelMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.6f
                                        ),
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
