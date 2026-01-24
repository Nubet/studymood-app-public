package pl.nubet.studymood.presentation.analyze

import kotlinx.datetime.LocalDate

sealed class AnalyzeEvent {
    data class MonthSelected(val yearMonth: YearMonth) : AnalyzeEvent()

    data class DayClicked(val date: LocalDate) : AnalyzeEvent()

    object RefreshData : AnalyzeEvent()

    object LoadMonthData : AnalyzeEvent()

    object ClearSelectedDay : AnalyzeEvent()
}
