package pl.nubet.studymood.presentation.analyze

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.domain.model.MoodCheckIn
import pl.nubet.studymood.domain.usecase.GetRecentMoods

class AnalyzeViewModel(
    private val getRecentMoods: GetRecentMoods,
    private val scopeProvider: MainScopeProvider,
    private val clockProvider: ClockProvider,
) {

    private val _state = MutableStateFlow(AnalyzeState())
    val state: StateFlow<AnalyzeState> = _state

    private var cachedCheckIns: List<MoodCheckIn> = emptyList()

    init {
        loadData()
    }

    fun onEvent(event: AnalyzeEvent) {
        when (event) {
            is AnalyzeEvent.MonthSelected -> {
                _state.update { it.copy(selectedMonth = event.yearMonth) }
                loadMonthData()
            }
            is AnalyzeEvent.DayClicked -> {
                val currentSelected = _state.value.selectedDate
                if (currentSelected == event.date) {
                    _state.update { it.copy(selectedDate = null, selectedDayMoods = emptyList()) }
                } else {
                    val moods = _state.value.monthCheckIns.filter { it.date == event.date }
                    _state.update { it.copy(selectedDate = event.date, selectedDayMoods = moods) }
                }
            }
            AnalyzeEvent.ClearSelectedDay -> {
                _state.update {
                    it.copy(selectedDate = null, selectedDayMoods = emptyList<MoodCheckIn>())
                }
            }
            AnalyzeEvent.RefreshData -> loadData()
            AnalyzeEvent.LoadMonthData -> loadMonthData()
        }
    }

    private fun loadData() {
        scopeProvider.scope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                val moodEntries = getRecentMoods(limit = 1000)
                val checkIns =
                    moodEntries.mapNotNull { entry -> convertToCheckIn(entry, ::logTransformError) }
                cachedCheckIns = checkIns

                val months = availableMonths(checkIns, todayDate())

                _state.update {
                    it.copy(
                        availableMonths = months,
                        selectedMonth = months.lastOrNull() ?: it.selectedMonth,
                        isLoading = false,
                    )
                }

                loadMonthData()
            } catch (e: Exception) {
                Log.e("Error loading analyze data: ${e.message}", e, tag = "AnalyzeViewModel")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadMonthData() {
        scopeProvider.scope.launch {
            try {
                val selectedMonth = _state.value.selectedMonth
                val checkIns =
                    cachedCheckIns.ifEmpty {
                        val allMoods = getRecentMoods(limit = 1000)
                        allMoods.mapNotNull { entry ->
                            convertToCheckIn(entry, ::logTransformError)
                        }
                    }

                val monthCheckIns = filterCheckInsByMonth(checkIns, selectedMonth)
                val quadrantCounts = quadrantCounts(monthCheckIns)
                val emotionFrequency = emotionFrequency(monthCheckIns)
                val calendarMatrix =
                    buildCalendarMatrix(
                        yearMonth = selectedMonth,
                        checkIns = monthCheckIns,
                        today = todayDate(),
                        onError = ::logTransformError,
                    )
                val timeOfDayDist = timeOfDayDistribution(monthCheckIns)

                _state.update {
                    it.copy(
                        monthCheckIns = monthCheckIns,
                        quadrantCounts = quadrantCounts,
                        emotionFrequency = emotionFrequency,
                        calendarMatrix = calendarMatrix,
                        timeOfDayDistribution = timeOfDayDist,
                    )
                }
            } catch (e: Exception) {
                Log.e("Error loading month data: ${e.message}", e, tag = "AnalyzeViewModel")
            }
        }
    }

    private fun todayDate(): LocalDate {
        val daysFromEpoch = (clockProvider.nowMillis() / (24 * 60 * 60 * 1000)).toInt()
        return LocalDate.fromEpochDays(daysFromEpoch)
    }

    private fun logTransformError(message: String, error: Exception) {
        Log.e(message, error, tag = "AnalyzeViewModel")
    }
}
