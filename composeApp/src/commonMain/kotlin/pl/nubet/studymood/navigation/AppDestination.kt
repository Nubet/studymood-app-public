package pl.nubet.studymood.navigation

sealed interface AppDestination {
    data object Splash : AppDestination

    data object Onboarding : AppDestination

    data class Main(val selectedTab: Tab = Tab.CheckIn) : AppDestination

    sealed interface Overlay : AppDestination {
        data class Breathing(val exerciseId: String) : Overlay

        data object Quotes : Overlay

        data object SelfTalk : Overlay

        data object BrightSpots : Overlay

        data object InterruptedPattern : Overlay

        data object DisappearingCenter : Overlay
    }
}

enum class Tab {
    CheckIn,
    Study,
    Reframe,
    Analyze,
    Settings,
}
