package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.domain.usecase.quotes.GetQuoteCategories
import pl.nubet.studymood.domain.usecase.quotes.GetQuotesByCategory
import pl.nubet.studymood.domain.usecase.quotes.ObserveSavedQuotes
import pl.nubet.studymood.domain.usecase.quotes.ToggleSaveQuote
import pl.nubet.studymood.domain.usecase.study.AddSubject
import pl.nubet.studymood.domain.usecase.study.DeleteSubject
import pl.nubet.studymood.domain.usecase.study.GetRecentSessions
import pl.nubet.studymood.domain.usecase.study.GetSubjects
import pl.nubet.studymood.domain.usecase.study.GetSubjectsWithSessionCount
import pl.nubet.studymood.domain.usecase.study.SaveStudySession
import pl.nubet.studymood.domain.usecase.study.SeedTestStudySessions
import pl.nubet.studymood.domain.usecase.study.UpdateSubject

data class StudyUseCases(
    val addSubject: AddSubject,
    val getSubjects: GetSubjects,
    val getSubjectsWithSessionCount: GetSubjectsWithSessionCount,
    val saveStudySession: SaveStudySession,
    val getRecentSessions: GetRecentSessions,
    val updateSubject: UpdateSubject,
    val deleteSubject: DeleteSubject,
    val seedTestStudySessions: SeedTestStudySessions,
)

data class MoodUseCases(
    val ensureLexiconSeeded: EnsureLexiconSeeded,
    val getEmotions: GetEmotions,
    val suggestLabels: SuggestLabels,
    val saveMoodEntry: SaveMoodEntry,
    val getRecentMoods: GetRecentMoods,
    val filterEmotions: FilterEmotionsUseCase,
)

data class QuotesUseCases(
    val getQuoteCategories: GetQuoteCategories,
    val getQuotesByCategory: GetQuotesByCategory,
    val toggleSaveQuote: ToggleSaveQuote,
    val observeSavedQuotes: ObserveSavedQuotes,
)
