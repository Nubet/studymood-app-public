package pl.nubet.studymood.di

import org.koin.dsl.module
import pl.nubet.studymood.app.AppInitViewModel
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.DefaultClockProvider
import pl.nubet.studymood.core.util.DefaultMainScopeProvider
import pl.nubet.studymood.core.util.DefaultRandomProvider
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.core.util.RandomProvider
import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.data.debug.DebugDataSeeder
import pl.nubet.studymood.data.repository.LexiconRepository
import pl.nubet.studymood.data.repository.LexiconRepositoryImpl
import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.data.repository.MoodRepositoryImpl
import pl.nubet.studymood.data.repository.OnboardingRepository
import pl.nubet.studymood.data.repository.OnboardingRepositoryImpl
import pl.nubet.studymood.data.repository.QuotesRepository
import pl.nubet.studymood.data.repository.QuotesRepositoryImpl
import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.StudyRepositoryImpl
import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.data.repository.SubjectRepositoryImpl
import pl.nubet.studymood.domain.usecase.EnsureLexiconSeeded
import pl.nubet.studymood.domain.usecase.ExportUserData
import pl.nubet.studymood.domain.usecase.FilterEmotionsUseCase
import pl.nubet.studymood.domain.usecase.GetEmotions
import pl.nubet.studymood.domain.usecase.GetExercisesByCategory
import pl.nubet.studymood.domain.usecase.GetMindToolCategories
import pl.nubet.studymood.domain.usecase.GetRecentMoods
import pl.nubet.studymood.domain.usecase.GetSuggestedExercise
import pl.nubet.studymood.domain.usecase.MoodUseCases
import pl.nubet.studymood.domain.usecase.QuotesUseCases
import pl.nubet.studymood.domain.usecase.SaveMoodEntry
import pl.nubet.studymood.domain.usecase.StudyUseCases
import pl.nubet.studymood.domain.usecase.SuggestLabels
import pl.nubet.studymood.domain.usecase.mood.BuildMoodEntry
import pl.nubet.studymood.domain.usecase.mood.CalculateMoodStats
import pl.nubet.studymood.domain.usecase.quotes.GetQuoteCategories
import pl.nubet.studymood.domain.usecase.quotes.GetQuotesByCategory
import pl.nubet.studymood.domain.usecase.quotes.ObserveSavedQuotes
import pl.nubet.studymood.domain.usecase.quotes.ToggleSaveQuote
import pl.nubet.studymood.domain.usecase.study.AddSubject
import pl.nubet.studymood.domain.usecase.study.DeleteSubject
import pl.nubet.studymood.domain.usecase.study.GetRecentSessions
import pl.nubet.studymood.domain.usecase.study.GetRecentSessionsWithSubjects
import pl.nubet.studymood.domain.usecase.study.GetSubjects
import pl.nubet.studymood.domain.usecase.study.GetSubjectsWithSessionCount
import pl.nubet.studymood.domain.usecase.study.SaveStudySession
import pl.nubet.studymood.domain.usecase.study.SeedTestStudySessions
import pl.nubet.studymood.domain.usecase.study.UpdateSubject
import pl.nubet.studymood.navigation.NavigationViewModel
import pl.nubet.studymood.presentation.analyze.AnalyzeViewModel
import pl.nubet.studymood.presentation.breathing.BreathingViewModel
import pl.nubet.studymood.presentation.brightspots.BrightSpotsViewModel
import pl.nubet.studymood.presentation.disappearingcenter.DisappearingCenterViewModel
import pl.nubet.studymood.presentation.interruptedpattern.InterruptedPatternViewModel
import pl.nubet.studymood.presentation.mindtools.MindToolsViewModel
import pl.nubet.studymood.presentation.mood.CheckInViewModel
import pl.nubet.studymood.presentation.onboarding.OnboardingViewModel
import pl.nubet.studymood.presentation.quotes.QuotesViewModel
import pl.nubet.studymood.presentation.selftalk.SelfTalkViewModel
import pl.nubet.studymood.presentation.settings.SettingsViewModel
import pl.nubet.studymood.presentation.study.SessionViewModel
import pl.nubet.studymood.presentation.study.StudyViewModel

val appModule = module {
    single<ClockProvider> { DefaultClockProvider() }
    single<RandomProvider> { DefaultRandomProvider() }
    factory<MainScopeProvider> { DefaultMainScopeProvider() }

    single { StudyMoodDatabase(get()) }

    single<LexiconRepository> { LexiconRepositoryImpl(get()) }
    single<MoodRepository> { MoodRepositoryImpl(get()) }
    single<SubjectRepository> { SubjectRepositoryImpl(get()) }
    single<StudyRepository> { StudyRepositoryImpl(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<QuotesRepository> { QuotesRepositoryImpl() }

    single { NavigationViewModel(get()) }
    single { AppInitViewModel(get(), get()) }

    factory { EnsureLexiconSeeded(get()) }
    factory { GetEmotions(get()) }
    factory { SuggestLabels(get()) }
    factory { SaveMoodEntry(get()) }
    factory { GetRecentMoods(get()) }
    factory { FilterEmotionsUseCase() }
    factory { BuildMoodEntry(get(), get()) }
    factory { CalculateMoodStats(get(), get()) }

    factory { AddSubject(get()) }
    factory { GetSubjects(get()) }
    factory { GetSubjectsWithSessionCount(get(), get()) }
    factory { SaveStudySession(get()) }
    factory { GetRecentSessions(get()) }
    factory { GetRecentSessionsWithSubjects(get(), get()) }
    factory { UpdateSubject(get()) }
    factory { DeleteSubject(get()) }
    factory { SeedTestStudySessions(get(), get()) }

    factory { GetQuoteCategories(get()) }
    factory { GetQuotesByCategory(get()) }
    factory { ToggleSaveQuote(get()) }
    factory { ObserveSavedQuotes(get()) }

    factory { GetMindToolCategories() }
    factory { GetExercisesByCategory() }
    factory { GetSuggestedExercise(get()) }

    factory { ExportUserData(get(), get(), get(), get()) }

    factory {
        MoodUseCases(
            ensureLexiconSeeded = get(),
            getEmotions = get(),
            suggestLabels = get(),
            saveMoodEntry = get(),
            getRecentMoods = get(),
            filterEmotions = get(),
        )
    }

    factory {
        StudyUseCases(
            addSubject = get(),
            getSubjects = get(),
            getSubjectsWithSessionCount = get(),
            saveStudySession = get(),
            getRecentSessions = get(),
            updateSubject = get(),
            deleteSubject = get(),
            seedTestStudySessions = get(),
        )
    }

    factory {
        QuotesUseCases(
            getQuoteCategories = get(),
            getQuotesByCategory = get(),
            toggleSaveQuote = get(),
            observeSavedQuotes = get(),
        )
    }

    factory { DebugDataSeeder(get(), get(), get()) }

    factory {
        val studyUseCases = get<StudyUseCases>()
        StudyViewModel(
            addSubject = studyUseCases.addSubject,
            getSubjectsWithCount = studyUseCases.getSubjectsWithSessionCount,
            saveStudy = studyUseCases.saveStudySession,
            getRecentSessionsWithSubjects = get(),
            updateSubject = studyUseCases.updateSubject,
            deleteSubject = studyUseCases.deleteSubject,
            scopeProvider = get(),
            clockProvider = get(),
        )
    }
    factory { params ->
        val studyUseCases = get<StudyUseCases>()
        SessionViewModel(
            subject = params.get(),
            saveStudy = studyUseCases.saveStudySession,
            scopeProvider = get(),
            clockProvider = get(),
        )
    }
    factory {
        val moodUseCases = get<MoodUseCases>()
        CheckInViewModel(
            ensureLexiconSeeded = moodUseCases.ensureLexiconSeeded,
            getEmotions = moodUseCases.getEmotions,
            filterEmotionsUseCase = moodUseCases.filterEmotions,
            suggestLabels = moodUseCases.suggestLabels,
            saveMood = moodUseCases.saveMoodEntry,
            getRecentMoods = moodUseCases.getRecentMoods,
            buildMoodEntry = get(),
            calculateMoodStats = get(),
            scopeProvider = get(),
            clockProvider = get(),
            randomProvider = get(),
        )
    }
    factory {
        val moodUseCases = get<MoodUseCases>()
        AnalyzeViewModel(
            getRecentMoods = moodUseCases.getRecentMoods,
            scopeProvider = get(),
            clockProvider = get(),
        )
    }
    factory {
        SettingsViewModel(
            onboardingRepo = get(),
            moodRepo = get(),
            studyRepo = get(),
            subjectRepo = get(),
            exportUserData = get(),
            debugDataSeeder = get(),
        )
    }
    factory { OnboardingViewModel(get(), get()) }
    factory {
        val quotesUseCases = get<QuotesUseCases>()
        QuotesViewModel(
            getCategoriesUseCase = quotesUseCases.getQuoteCategories,
            getQuotesUseCase = quotesUseCases.getQuotesByCategory,
            toggleSaveUseCase = quotesUseCases.toggleSaveQuote,
            observeSavedUseCase = quotesUseCases.observeSavedQuotes,
        )
    }
    factory { MindToolsViewModel(get(), get(), get(), get()) }
    factory { params -> BreathingViewModel(params.get(), get()) }
    factory { SelfTalkViewModel() }
    factory { BrightSpotsViewModel() }
    factory { InterruptedPatternViewModel(get(), get()) }
    factory { DisappearingCenterViewModel(get()) }
}
