package pl.nubet.studymood.ui.screens.quotes

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.presentation.quotes.QuotesEvent
import pl.nubet.studymood.presentation.quotes.QuotesViewModel
import pl.nubet.studymood.presentation.quotes.SwipeDirection
import pl.nubet.studymood.ui.screens.quotes.components.*
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun QuotesScreen(
    onBackClick: () -> Unit,
    padding: PaddingValues,
    viewModel: QuotesViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val dimens = LocalDimens.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(state.isCopyFeedbackActive) {
        if (state.isCopyFeedbackActive && state.currentQuote != null) {
            val quote = state.currentQuote!!
            val text = "\"${quote.text}\" — ${quote.author}"
            clipboardManager.setText(AnnotatedString(text))
        }
    }

    Box(
        modifier =
            modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(padding)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = dimens.x24, vertical = dimens.x16)
                ) {
                    QuotesTopBar(
                        categoryTitle = state.currentCategory.name,
                        isMenuOpen = state.isMenuOpen,
                        onBackClick = {
                            viewModel.onEvent(QuotesEvent.OnBackClicked)
                            onBackClick()
                        },
                        onCategoryClick = { viewModel.onEvent(QuotesEvent.OnMenuToggled) },
                        onHelpClick = { viewModel.onEvent(QuotesEvent.OnHelpClicked) },
                    )
                }
            },
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(scaffoldPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier =
                        Modifier.weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = dimens.x24)
                            .pointerInput(state.selectedCategoryIndex, state.currentQuoteIndex) {
                                var offsetX = 0f
                                var offsetY = 0f

                                detectDragGestures(
                                    onDragEnd = {
                                        val threshold = 40.dp.toPx()

                                        if (kotlin.math.abs(offsetX) > kotlin.math.abs(offsetY)) {
                                            if (kotlin.math.abs(offsetX) > threshold) {
                                                if (offsetX > 0) {
                                                    viewModel.onEvent(
                                                        QuotesEvent.OnSwipeHorizontal(
                                                            SwipeDirection.PREV
                                                        )
                                                    )
                                                } else {
                                                    viewModel.onEvent(
                                                        QuotesEvent.OnSwipeHorizontal(
                                                            SwipeDirection.NEXT
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            if (kotlin.math.abs(offsetY) > threshold) {
                                                if (offsetY > 0) {
                                                    viewModel.onEvent(
                                                        QuotesEvent.OnSwipeVertical(
                                                            SwipeDirection.DOWN
                                                        )
                                                    )
                                                } else {
                                                    viewModel.onEvent(
                                                        QuotesEvent.OnSwipeVertical(
                                                            SwipeDirection.UP
                                                        )
                                                    )
                                                }
                                            }
                                        }

                                        offsetX = 0f
                                        offsetY = 0f
                                    },
                                    onDrag = { _, dragAmount ->
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y
                                    },
                                )
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    QuoteCard(
                        quote = state.currentQuote,
                        animationKey = state.selectedCategoryIndex to state.currentQuoteIndex,
                        isVerticalSwipe = state.isVerticalSwipe,
                    )
                }

                QuotesBottomActions(
                    isSaved = state.isCurrentQuoteSaved,
                    isCopyFeedbackActive = state.isCopyFeedbackActive,
                    onSaveClick = { viewModel.onEvent(QuotesEvent.OnSaveToggled) },
                    onCopyClick = { viewModel.onEvent(QuotesEvent.OnCopyClicked) },
                )

                Spacer(modifier = Modifier.height(dimens.x24))
            }
        }

        if (state.isMenuOpen) {
            CategoryDropdownMenu(
                categories = state.categories,
                selectedIndex = state.selectedCategoryIndex,
                isVisible = true,
                onCategoryClick = { index ->
                    viewModel.onEvent(QuotesEvent.OnCategorySelected(index))
                },
                onDismiss = { viewModel.onEvent(QuotesEvent.OnMenuDismissed) },
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (state.isHelpModalOpen) {
            QuotesHelpModal(
                isVisible = true,
                onDismiss = { viewModel.onEvent(QuotesEvent.OnHelpDismissed) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
