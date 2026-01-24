package pl.nubet.studymood.ui.screens.onboarding.slides

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pl.nubet.studymood.domain.model.FocusArea
import pl.nubet.studymood.domain.model.ReminderTime
import pl.nubet.studymood.ui.theme.LocalDimens
import studymood.composeapp.generated.resources.*

@Composable
fun NameInputSlide(name: String, onNameChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Profile",
        headline = "What should we call you?",
        bodyText = "Your name helps us keep the app feeling personal and warm.",
        heroCard = { NameInputHeroCard(name = name, onNameChange = onNameChange) },
        modifier = modifier,
    )
}

@Composable
private fun NameInputHeroCard(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingHeroCard(
        cardTitle = "",
        cardSubtitle = "",
        illustration = painterResource(Res.drawable.waving_guy),
        illustrationOffset = (-30).dp,
        illustrationBottomOffset = (-10).dp,
        illustrationSize = 160.dp,
        illustrationRotation = -8f,
        contentScale = ContentScale.Fit,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Name",
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                )

                Spacer(modifier = Modifier.height(6.dp))

                BasicTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Column {
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                                if (name.isEmpty()) {
                                    Text(
                                        text = "Enter name...",
                                        style =
                                            MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = 16.sp,
                                                color =
                                                    MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                    ),
                                            ),
                                    )
                                }
                                innerTextField()
                            }

                            Box(
                                modifier =
                                    Modifier.fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.outline)
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You will see it in greetings like \"Hi, Alex\".",
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        ),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun FocusAreaSlide(
    selectedArea: FocusArea,
    onSelectArea: (FocusArea) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingSlideLayout(
        tagline = "Focus",
        headline = "What feels most important right now?",
        bodyText =
            "There’s no right way to use this app. Start with what feels most relevant to you right now.",
        heroCard = { FocusAreaHeroCard(selectedArea = selectedArea, onSelectArea = onSelectArea) },
        modifier = modifier,
    )
}

@Composable
private fun FocusAreaHeroCard(
    selectedArea: FocusArea,
    onSelectArea: (FocusArea) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    OnboardingHeroCard(
        cardTitle = "Choose a starting focus",
        cardSubtitle = "",
        illustration = painterResource(Res.drawable.undraw_notify_rnwe),
        illustrationOffset = (-40).dp,
        illustrationBottomOffset = (-30).dp,
        illustrationSize = 200.dp,
        illustrationRotation = -5f,
        contentScale = ContentScale.Fit,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimens.x8),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimens.x8),
                ) {
                    OnboardingChip(
                        text = "Emotions and mood",
                        isSelected = selectedArea == FocusArea.MOOD,
                        onClick = { onSelectArea(FocusArea.MOOD) },
                        modifier = Modifier.weight(1f),
                    )

                    OnboardingChip(
                        text = "Study and focus",
                        isSelected = selectedArea == FocusArea.STUDY,
                        onClick = { onSelectArea(FocusArea.STUDY) },
                        modifier = Modifier.weight(1f),
                    )
                }

                OnboardingChip(
                    text = "Mood + study",
                    isSelected = selectedArea == FocusArea.BOTH,
                    onClick = { onSelectArea(FocusArea.BOTH) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun ReminderSlide(
    selectedTime: ReminderTime,
    onSelectTime: (ReminderTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingSlideLayout(
        tagline = "Reminder",
        headline = "When should we nudge you to check in?",
        bodyText =
            "A gentle reminder once a day keeps your mood history accurate without feeling pushy.",
        heroCard = { ReminderHeroCard(selectedTime = selectedTime, onSelectTime = onSelectTime) },
        modifier = modifier,
    )
}

@Composable
private fun ReminderHeroCard(
    selectedTime: ReminderTime,
    onSelectTime: (ReminderTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingHeroCard(
        cardTitle = "Choose a time",
        cardSubtitle = "",
        illustration = painterResource(Res.drawable.cloack),
        illustrationOffset = (-10).dp,
        illustrationBottomOffset = 10.dp,
        illustrationSize = 180.dp,
        illustrationRotation = -5f,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                ReminderTime.entries.forEach { time ->
                    OnboardingChip(
                        text = time.displayName,
                        isSelected = selectedTime == time,
                        onClick = { onSelectTime(time) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "You can adjust notifications later in settings.",
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        ),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun AgePickerSlide(age: Int, onAgeChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    OnboardingSlideLayout(
        tagline = "Profile",
        headline = "What is your age?",
        bodyText =
            "This helps keep insights and recommendations aligned with where you are in life.",
        heroCard = { AgePickerHeroCard(age = age, onAgeChange = onAgeChange) },
        modifier = modifier,
    )
}

@Composable
private fun AgePickerHeroCard(age: Int, onAgeChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    val MIN_AGE = 13
    val MAX_AGE = 70
    val ages = remember { (MIN_AGE..MAX_AGE).toList() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun findClosestItemToCenter(): Pair<Int?, Int?> {
        val layoutInfo = listState.layoutInfo
        val visible = layoutInfo.visibleItemsInfo
        if (visible.isEmpty()) return null to null

        val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        val closest =
            visible.minByOrNull { item -> abs((item.offset + item.size / 2) - viewportCenter) }

        return closest?.let { it.index to ages.getOrNull(it.index) } ?: (null to null)
    }

    val selectedAge by remember {
        derivedStateOf {
            val (_, closestAge) = findClosestItemToCenter()
            closestAge ?: age
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            delay(80)
            coroutineScope.launch {
                val layoutInfo = listState.layoutInfo
                val visible = layoutInfo.visibleItemsInfo
                if (visible.isEmpty()) return@launch

                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val closest =
                    visible.minByOrNull { item ->
                        abs((item.offset + item.size / 2) - viewportCenter)
                    }

                closest?.let { item ->
                    val targetAge = ages[item.index]

                    if (targetAge != age) {
                        onAgeChange(targetAge)
                    }

                    val itemCenter = item.offset + (item.size / 2)
                    val delta = itemCenter - viewportCenter

                    if (abs(delta) > 1) {
                        listState.animateScrollBy(delta.toFloat())
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val initialIndex = ages.indexOf(age).takeIf { it >= 0 } ?: ages.indexOf(18)

        delay(100)
        while (listState.layoutInfo.visibleItemsInfo.isEmpty()) {
            delay(10)
        }

        coroutineScope.launch {
            val layoutInfo = listState.layoutInfo
            val itemHeight = layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 52
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            val targetItem = layoutInfo.visibleItemsInfo.find { it.index == initialIndex }
            if (targetItem != null) {
                val itemCenter = targetItem.offset + (targetItem.size / 2)
                val delta = itemCenter - viewportCenter
                listState.scrollBy(delta.toFloat())
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            val rowHeight = 104.dp
            val verticalPadding = (maxHeight - rowHeight) / 2

            Box(
                modifier =
                    Modifier.align(Alignment.Center)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(76.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            brush =
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFFFD28A), Color(0xFFFFB347))
                                )
                        )
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = verticalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(ages, key = { it }) { itemAge ->
                    AgeItem(
                        age = itemAge,
                        isSelected = itemAge == selectedAge,
                        listState = listState,
                    )
                }
            }

            Box(
                modifier =
                    Modifier.align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme.background,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                        )
                                )
                        )
            )

            Box(
                modifier =
                    Modifier.align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                            MaterialTheme.colorScheme.background,
                                        )
                                )
                        )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text =
                "Swipe up or down to set your age. The big number in the frame is the one we use.",
            style =
                MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                ),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun AgeItem(
    age: Int,
    isSelected: Boolean,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val distanceFromCenter by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            if (visible.isEmpty()) return@derivedStateOf 1000f

            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val itemInfo = visible.find { it.key == age } ?: return@derivedStateOf 1000f

            abs((itemInfo.offset + itemInfo.size / 2) - viewportCenter).toFloat()
        }
    }

    val deepCoffee = Color(0xFF221913)

    data class DepthState(
        val fontSize: Float,
        val opacity: Float,
        val scale: Float,
        val color: Color,
        val weight: FontWeight,
    )

    val stepPx = 52f

    val selectedThreshold = kotlin.math.max(6f, stepPx * 0.14f)
    val oneAwayThreshold = stepPx * 1.35f
    val twoAwayThreshold = stepPx * 2.70f

    val depthState =
        when {
            distanceFromCenter < selectedThreshold ->
                DepthState(
                    fontSize = 44f,
                    opacity = 1f,
                    scale = 1.12f,
                    color = Color.White,
                    weight = FontWeight.ExtraBold,
                )
            distanceFromCenter < oneAwayThreshold ->
                DepthState(
                    fontSize = 56f,
                    opacity = 0.85f,
                    scale = 0.95f,
                    color = deepCoffee.copy(alpha = 0.95f),
                    weight = FontWeight.Bold,
                )
            distanceFromCenter < twoAwayThreshold ->
                DepthState(
                    fontSize = 40f,
                    opacity = 0.55f,
                    scale = 0.86f,
                    color = deepCoffee.copy(alpha = 0.85f),
                    weight = FontWeight.SemiBold,
                )
            else ->
                DepthState(
                    fontSize = 32f,
                    opacity = 0.30f,
                    scale = 0.76f,
                    color = deepCoffee.copy(alpha = 0.70f),
                    weight = FontWeight.Medium,
                )
        }

    val animatedFontSize by
        animateFloatAsState(
            targetValue = depthState.fontSize,
            animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        )
    val animatedOpacity by
        animateFloatAsState(
            targetValue = depthState.opacity,
            animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        )
    val animatedScale by
        animateFloatAsState(
            targetValue = depthState.scale,
            animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        )

    Box(modifier = modifier.height(104.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = age.toString(),
            style =
                MaterialTheme.typography.displaySmall.copy(
                    fontSize = animatedFontSize.sp,
                    fontWeight = depthState.weight,
                    color = depthState.color,
                    textAlign = TextAlign.Center,
                    letterSpacing = if (distanceFromCenter < selectedThreshold) 0.04.sp else 0.sp,
                ),
            modifier = Modifier.alpha(animatedOpacity).scale(animatedScale),
        )
    }
}

@Composable
fun OnboardingChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        } else {
            Color.White.copy(alpha = 0.88f)
        }

    val borderColor =
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        }

    val textColor =
        if (isSelected) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(50))
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(50))
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp, color = textColor),
        )
    }
}
