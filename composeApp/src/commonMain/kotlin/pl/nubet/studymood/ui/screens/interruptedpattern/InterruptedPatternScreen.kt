package pl.nubet.studymood.ui.screens.interruptedpattern

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternEvent
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternViewModel
import pl.nubet.studymood.ui.screens.interruptedpattern.components.*

@Composable
fun InterruptedPatternScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InterruptedPatternViewModel = koinInject(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.onEvent(InterruptedPatternEvent.PhaseTimerTick)
            delay(16)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PatternTopBar(
                title = "Mind tool",
                subtitle = "Interrupted pattern",
                onBack = {
                    viewModel.onEvent(InterruptedPatternEvent.BackClicked)
                    onBack()
                },
            )

            Spacer(Modifier.height(10.dp))

            HeadlineBlock(mainText = state.mainText, helperText = state.helperText)

            Spacer(Modifier.height(18.dp))

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                DrawingCard(
                    state = state,
                    onPointerEvent = { event -> viewModel.onEvent(event) },
                    onCanvasSizeChanged = { width, height ->
                        viewModel.updateCanvasSize(width, height)
                    },
                )
            }

            Spacer(Modifier.height(4.dp))

            BottomBar(
                showDoneButton = state.showDoneButton,
                onDoneClick = {
                    if (state.isFinished) {
                        onBack()
                    } else {
                        viewModel.onEvent(InterruptedPatternEvent.DoneClicked)
                    }
                },
            )
        }
    }
}
