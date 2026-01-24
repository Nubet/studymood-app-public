package pl.nubet.studymood.ui.screens.reframe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.presentation.mindtools.MindToolsEvent
import pl.nubet.studymood.presentation.mindtools.MindToolsViewModel
import pl.nubet.studymood.ui.screens.reframe.components.CategoryCard
import pl.nubet.studymood.ui.screens.reframe.components.ExerciseBottomSheet
import pl.nubet.studymood.ui.screens.reframe.components.SuggestionCard
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun ReframeScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    onNavigateToBreathing: (exerciseId: String) -> Unit = {},
    onNavigateToQuotes: () -> Unit = {},
    onNavigateToSelfTalk: () -> Unit = {},
    onNavigateToBrightSpots: () -> Unit = {},
    onNavigateToInterruptedPattern: () -> Unit = {},
    onNavigateToDisappearingCenter: () -> Unit = {},
    viewModel: MindToolsViewModel = koinInject(),
) {

    val state by viewModel.state.collectAsState()
    val dimens = LocalDimens.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = dimens.x16),
            verticalArrangement = Arrangement.spacedBy(dimens.x12),
        ) {
            Spacer(modifier = Modifier.height(dimens.x8))

            Text(
                text = "Mind Tools",
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
            )

            Text(
                text = state.randomMicrocopy,
                style =
                    MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp,
                    ),
            )

            Spacer(modifier = Modifier.height(dimens.x8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier.size(6.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                                    CircleShape,
                                )
                    )
                    Text(
                        text = "SUGGESTED",
                        style =
                            MaterialTheme.typography.labelMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 0.4.sp,
                            ),
                    )
                }

                Text(
                    text = "2 min",
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    modifier =
                        Modifier.background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(50),
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                RoundedCornerShape(50),
                            )
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                )
            }

            state.suggestedExercise?.let { (category, exercise) ->
                SuggestionCard(
                    category = category,
                    exercise = exercise,
                    onClick = { viewModel.onEvent(MindToolsEvent.SuggestionClicked(category.id)) },
                )
            }

            Spacer(modifier = Modifier.height(dimens.x8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier.size(6.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                                    CircleShape,
                                )
                    )
                    Text(
                        text = "CATEGORIES",
                        style =
                            MaterialTheme.typography.labelMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 0.4.sp,
                            ),
                    )
                }

                Text(
                    text = "Tap to open",
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    modifier =
                        Modifier.background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(50),
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                RoundedCornerShape(50),
                            )
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.x12),
            ) {
                state.categories.chunked(2).forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimens.x12),
                    ) {
                        rowCategories.forEach { category ->
                            CategoryCard(
                                category = category,
                                onClick = {
                                    if (category.id == "quotes") {
                                        onNavigateToQuotes()
                                    } else {
                                        viewModel.onEvent(MindToolsEvent.CategoryClicked(category))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (rowCategories.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimens.x16))
        }

        if (state.isBottomSheetVisible && state.selectedCategory != null) {
            ExerciseBottomSheet(
                category = state.selectedCategory!!,
                exercises = state.exercises,
                bottomPadding = padding.calculateBottomPadding(),
                onStartClick = { exerciseId ->
                    Log.d("onStartClick called with exerciseId: $exerciseId", tag = "ReframeScreen")
                    val exercise = state.exercises.find { it.id == exerciseId }
                    Log.d(
                        "Found exercise: ${exercise?.title}, requiresFullScreen: ${exercise?.requiresFullScreen}",
                        tag = "ReframeScreen",
                    )

                    if (exercise?.requiresFullScreen == true) {
                        Log.d("Navigating to breathing screen", tag = "ReframeScreen")
                        viewModel.onEvent(MindToolsEvent.CloseBottomSheet)
                        onNavigateToBreathing(exerciseId)
                    } else if (exerciseId == "self_talk") {
                        Log.d("Navigating to self talk screen", tag = "ReframeScreen")
                        viewModel.onEvent(MindToolsEvent.CloseBottomSheet)
                        onNavigateToSelfTalk()
                    } else if (exerciseId == "bright_spots") {
                        Log.d("Navigating to bright spots screen", tag = "ReframeScreen")
                        viewModel.onEvent(MindToolsEvent.CloseBottomSheet)
                        onNavigateToBrightSpots()
                    } else if (exerciseId == "interrupted_pattern") {
                        Log.d("Navigating to interrupted pattern screen", tag = "ReframeScreen")
                        viewModel.onEvent(MindToolsEvent.CloseBottomSheet)
                        onNavigateToInterruptedPattern()
                    } else if (exerciseId == "disappearing_center") {
                        Log.d("Navigating to disappearing center screen", tag = "ReframeScreen")
                        viewModel.onEvent(MindToolsEvent.CloseBottomSheet)
                        onNavigateToDisappearingCenter()
                    } else {
                        viewModel.onEvent(MindToolsEvent.StartExercise(exerciseId))
                    }
                },
                onDismiss = { viewModel.onEvent(MindToolsEvent.CloseBottomSheet) },
            )
        }
    }
}
