package pl.nubet.studymood.ui.screens.checkin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.mood.CheckInUiEvent
import pl.nubet.studymood.presentation.mood.CheckInViewModel
import pl.nubet.studymood.ui.components.AppChip
import pl.nubet.studymood.ui.components.BreatheCircleButton
import pl.nubet.studymood.ui.components.PrimaryButton
import pl.nubet.studymood.ui.components.StatCapsule
import pl.nubet.studymood.ui.components.WaveText
import pl.nubet.studymood.ui.components.interaction.springMediumBouncy
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens
import pl.nubet.studymood.ui.theme.LocalQuadrantColors

@Composable
fun CheckInScreen(modifier: Modifier = Modifier, padding: PaddingValues) {
    val vm: CheckInViewModel = koinInject()

    val state by vm.state.collectAsState()

    val dimens = LocalDimens.current

    when {
        state.isPreCheck -> {
            Column(modifier = modifier.fillMaxSize().padding(padding).padding(dimens.x16)) {
                EntryCheckInScreen(
                    total = state.totalMoodsLogged,
                    streak = state.currentStreakDays,
                    onStart = { vm.onEvent(CheckInUiEvent.StartCheckIn) },
                )
            }
            return
        }
        state.showContextScreen -> {
            ContextScreen(
                emotionLabel = state.selectedEmotion?.label ?: "",
                onComplete = { activity, companion, location ->
                    vm.onEvent(CheckInUiEvent.OnContextComplete(activity, companion, location))
                },
                modifier = modifier.padding(padding),
            )
            return
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = dimens.x20, vertical = dimens.x24)
    ) {
        Spacer(Modifier.height(dimens.x16))

        val prompts =
            listOf(
                "How does your day feel so far?",
                "What's your current vibe?",
                "Where are you emotionally?",
                "What is the tone of this moment?",
            )
        val promptIndex =
            remember(state.totalMoodsLogged, state.currentStreakDays) {
                (state.totalMoodsLogged + state.currentStreakDays).mod(prompts.size)
            }
        val headerText = prompts[promptIndex]

        Text(
            headerText,
            style = LocalAppTypography.current.h1,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Pleasant ${state.pleasant} • Energy ${state.energy}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        val selected = state.selectedEmotion
        if (selected != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = selected.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            )
        }

        Spacer(Modifier.height(dimens.x24))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            MoodCanvas(
                pleasant = state.pleasant,
                energy = state.energy,
                onChange = { p, e -> vm.onEvent(CheckInUiEvent.OnPointChange(p, e)) },
            )
            Spacer(Modifier.height(dimens.x12))
            Text(
                "Pleasant 0–100",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Energy 0–100",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(dimens.x20))

        Text(
            "Suggested labels",
            style = LocalAppTypography.current.h3,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(dimens.x12))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.suggestions) { emotion ->
                AppChip(
                    label = emotion.label,
                    selected = state.selectedEmotion?.label == emotion.label,
                    onClick = { vm.onEvent(CheckInUiEvent.OnSelectEmotion(emotion)) },
                )
            }
        }

        Spacer(Modifier.height(dimens.x12))

        TextButton(onClick = { vm.onEvent(CheckInUiEvent.OpenPicker) }) {
            Text("See the full list", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(dimens.x24))

        PrimaryButton(
            onClick = { vm.onEvent(CheckInUiEvent.ContinueToContext) },
            enabled = state.selectedEmotion != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Continue")
        }

        if (state.selectedEmotion == null) {
            Spacer(Modifier.height(dimens.x8))
            Text(
                "Select an emotion to continue",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        state.errorMsg?.let { msg ->
            Spacer(Modifier.height(dimens.x8))
            Text(
                msg,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(dimens.x16))
    }

    if (state.showEmotionPicker) {
        EmotionPickerScreen(
            emotions = state.filteredEmotions,
            onEmotionSelect = { emotion ->
                vm.onEvent(CheckInUiEvent.OnSelectEmotion(emotion))
                vm.onEvent(CheckInUiEvent.ClosePicker)
            },
            onDismiss = { vm.onEvent(CheckInUiEvent.ClosePicker) },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun EntryCheckInScreen(total: Int, streak: Int, onStart: () -> Unit) {
    val dimens = LocalDimens.current
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = dimens.x16),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimens.x8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatCapsule(
                label = "Total moods",
                value = total.toString(),
                modifier = Modifier.weight(1f),
            )
            StatCapsule(label = "Streak", value = "$streak days", modifier = Modifier.weight(1f))
        }

        BreatheCircleButton(onClick = onStart, size = 200.dp, label = "Check-in")

        val micro =
            listOf("Take a moment", "Check in with yourself", "One small pause", "Let's reflect")
        val idx = remember(total, streak) { (total + streak) % micro.size }
        WaveText(
            text = micro[idx],
            modifier = Modifier.padding(bottom = dimens.x8),
            amplitude = 3.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MoodCanvas(
    pleasant: Int,
    energy: Int,
    onChange: (pleasant: Int, energy: Int) -> Unit,
    boxSize: Dp = 260.dp,
) {
    val colors = LocalQuadrantColors.current
    var widthPx by remember { mutableStateOf(1f) }
    var heightPx by remember { mutableStateOf(1f) }
    var dragging by remember { mutableStateOf(false) }

    val pleasantF by
        animateFloatAsState(
            targetValue = pleasant / 100f,
            animationSpec = springMediumBouncy(),
            label = "pleasant-anim",
        )
    val energyF by
        animateFloatAsState(
            targetValue = energy / 100f,
            animationSpec = springMediumBouncy(),
            label = "energy-anim",
        )

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(
            modifier =
                Modifier.size(boxSize)
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val (p, e) = offsetToValues(offset, widthPx, heightPx)
                            onChange(p, e)
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragging = true },
                            onDragEnd = { dragging = false },
                            onDragCancel = { dragging = false },
                            onDrag = { change, _ ->
                                val (p, e) = offsetToValues(change.position, widthPx, heightPx)
                                onChange(p, e)
                            },
                        )
                    }
        ) {
            val dsSize = this.size
            widthPx = dsSize.width
            heightPx = dsSize.height

            val halfW = size.width / 2f
            val halfH = size.height / 2f
            drawRect(
                color = colors.overlayHighEnergyUnpleasant,
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(halfW, halfH),
            )
            drawRect(
                color = colors.overlayHighPleasantHighEnergy,
                topLeft = Offset(halfW, 0f),
                size = androidx.compose.ui.geometry.Size(halfW, halfH),
            )
            drawRect(
                color = colors.overlayLowEnergyUnpleasant,
                topLeft = Offset(0f, halfH),
                size = androidx.compose.ui.geometry.Size(halfW, halfH),
            )
            drawRect(
                color = colors.overlayLowEnergyPleasant,
                topLeft = Offset(halfW, halfH),
                size = androidx.compose.ui.geometry.Size(halfW, halfH),
            )

            val steps = 5
            val stroke = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f
            val stepX = cx / steps
            val stepY = cy / steps
            val gridColor = colors.gridColor
            val axisColor = colors.axisColor

            for (n in 1..steps) {
                val xRight = cx + n * stepX
                val xLeft = cx - n * stepX
                if (xRight in 0f..w)
                    drawLine(
                        gridColor,
                        Offset(xRight, 0f),
                        Offset(xRight, h),
                        strokeWidth = stroke.width,
                    )
                if (xLeft in 0f..w)
                    drawLine(
                        gridColor,
                        Offset(xLeft, 0f),
                        Offset(xLeft, h),
                        strokeWidth = stroke.width,
                    )
            }
            for (n in 1..steps) {
                val yDown = cy + n * stepY
                val yUp = cy - n * stepY
                if (yDown in 0f..h)
                    drawLine(
                        gridColor,
                        Offset(0f, yDown),
                        Offset(w, yDown),
                        strokeWidth = stroke.width,
                    )
                if (yUp in 0f..h)
                    drawLine(gridColor, Offset(0f, yUp), Offset(w, yUp), strokeWidth = stroke.width)
            }

            drawLine(axisColor, Offset(w / 2f, 0f), Offset(w / 2f, h), strokeWidth = 1.dp.toPx())
            drawLine(axisColor, Offset(0f, h / 2f), Offset(w, h / 2f), strokeWidth = 1.dp.toPx())

            val px = pleasantF * w
            val py = (1f - energyF) * h
            val baseGlow = if (dragging) 0.30f else 0.16f
            drawCircle(color = colors.pointGlow, radius = 18.dp.toPx(), center = Offset(px, py))
            drawCircle(
                color = colors.pointOuter.copy(alpha = baseGlow + 0.4f),
                radius = 12.dp.toPx(),
                center = Offset(px, py),
            )
            drawCircle(color = colors.pointOuter, radius = 8.dp.toPx(), center = Offset(px, py))
            drawCircle(colors.pointInner, radius = 3.dp.toPx(), center = Offset(px, py))
        }
    }
}

private fun offsetToValues(offset: Offset, widthPx: Float, heightPx: Float): Pair<Int, Int> {
    val x = offset.x.coerceIn(0f, widthPx)
    val y = offset.y.coerceIn(0f, heightPx)
    val pleasant = ((x / widthPx) * 100f).toInt()
    val energy = (100f - (y / heightPx) * 100f).toInt()
    return Pair(pleasant.coerceIn(0, 100), energy.coerceIn(0, 100))
}
