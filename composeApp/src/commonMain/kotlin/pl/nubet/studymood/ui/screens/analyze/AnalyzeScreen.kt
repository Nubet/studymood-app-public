package pl.nubet.studymood.ui.screens.analyze

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.analyze.AnalyzeViewModel
import pl.nubet.studymood.ui.screens.analyze.components.*
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun AnalyzeScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    viewModel: AnalyzeViewModel = koinInject(),
) {
    val state by viewModel.state.collectAsState()
    val dimens = LocalDimens.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimens.x16),
        verticalArrangement = Arrangement.spacedBy(dimens.x24),
    ) {
        Spacer(modifier = Modifier.height(dimens.x8))

        AnalyzeHeader()
        MonthSelector(
            availableMonths = state.availableMonths,
            selectedMonth = state.selectedMonth,
            onMonthSelected = {
                viewModel.onEvent(
                    pl.nubet.studymood.presentation.analyze.AnalyzeEvent.MonthSelected(it)
                )
            },
        )

        QuadrantBreakdownSection(
            quadrantCounts = state.quadrantCounts,
            totalCheckIns = state.monthCheckIns.size,
        )

        CalendarViewSection(
            calendarMatrix = state.calendarMatrix,
            selectedDate = state.selectedDate,
            onDayClicked = {
                viewModel.onEvent(
                    pl.nubet.studymood.presentation.analyze.AnalyzeEvent.DayClicked(it)
                )
            },
            selectedDayMoods = state.selectedDayMoods,
        )

        EmotionFrequencySection(emotionFrequency = state.emotionFrequency)

        TimeOfDaySection(timeOfDayDistribution = state.timeOfDayDistribution)

        state.studyInsight?.let { insight -> StudyInsightSection(insight = insight) }

        Spacer(modifier = Modifier.height(dimens.x24))
    }
}
