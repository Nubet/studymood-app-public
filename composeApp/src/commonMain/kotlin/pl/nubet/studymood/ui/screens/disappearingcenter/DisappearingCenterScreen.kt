package pl.nubet.studymood.ui.screens.disappearingcenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.disappearingcenter.DisappearingCenterEvent
import pl.nubet.studymood.presentation.disappearingcenter.DisappearingCenterViewModel
import pl.nubet.studymood.ui.screens.disappearingcenter.components.*

@Composable
fun DisappearingCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DisappearingCenterViewModel = koinInject(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.onEvent(DisappearingCenterEvent.PhaseTimerTick)
            delay(16)
        }
    }

    LaunchedEffect(state.isFinished) { if (state.isFinished) {} }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CenterTopBar(
                onBackClick = {
                    viewModel.onEvent(DisappearingCenterEvent.BackClicked)
                    onBack()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CenterHeadlineBlock(
                mainText = state.mainText,
                helperText = state.helperText,
                modifier = Modifier.padding(top = 4.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                CenterDrawingCard(
                    state = state,
                    onEvent = { event -> viewModel.onEvent(event) },
                    onCanvasSizeChanged = { width, height ->
                        viewModel.updateCanvasSize(width, height)
                    },
                )
            }

            CenterBottomBar(
                showDoneButton = state.showDoneButton,
                isFinished = state.isFinished,
                onDoneClick = {
                    if (state.isFinished) {
                        onBack()
                    } else {
                        viewModel.onEvent(DisappearingCenterEvent.DoneClicked)
                    }
                },
            )
        }
    }
}
