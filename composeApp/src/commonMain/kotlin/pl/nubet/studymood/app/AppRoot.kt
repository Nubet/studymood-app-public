package pl.nubet.studymood.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.nubet.studymood.domain.model.BreathingExerciseType
import pl.nubet.studymood.navigation.AppDestination
import pl.nubet.studymood.navigation.NavigationViewModel
import pl.nubet.studymood.navigation.Tab
import pl.nubet.studymood.presentation.quotes.QuotesViewModel
import pl.nubet.studymood.ui.screens.analyze.AnalyzeScreen
import pl.nubet.studymood.ui.screens.breathing.BreathingScreen
import pl.nubet.studymood.ui.screens.checkin.CheckInScreen
import pl.nubet.studymood.ui.screens.onboarding.OnboardingScreen
import pl.nubet.studymood.ui.screens.quotes.QuotesScreen
import pl.nubet.studymood.ui.screens.reframe.ReframeScreen
import pl.nubet.studymood.ui.screens.study.StudyScreen

private val tabConfigs =
    mapOf(
        Tab.CheckIn to TabConfig("Check in", Icons.Outlined.CheckCircle),
        Tab.Study to TabConfig("Study", Icons.Outlined.Timer),
        Tab.Reframe to TabConfig("Reframe", Icons.Outlined.Lightbulb),
        Tab.Analyze to TabConfig("Analyze", Icons.Outlined.BarChart),
        Tab.Settings to TabConfig("Settings", Icons.Outlined.Settings),
    )

private data class TabConfig(val label: String, val icon: ImageVector)

@Composable
fun AppRoot() {
    val navViewModel: NavigationViewModel = koinInject()
    val destination by navViewModel.currentDestination.collectAsState()
    val overlays by navViewModel.overlays.collectAsState()

    when (val dest = destination) {
        is AppDestination.Splash -> {
            pl.nubet.studymood.ui.screens.splash.SplashScreen(
                onComplete = {
                }
            )
        }
        is AppDestination.Onboarding -> {
            OnboardingScreen(onComplete = { navViewModel.navigate(AppDestination.Main()) })
        }
        is AppDestination.Main -> {
            MainContent(
                selectedTab = dest.selectedTab,
                onTabSelected = { navViewModel.selectTab(it) },
                onNavigateToOverlay = { overlay -> navViewModel.navigate(overlay) },
                onPopOverlay = { navViewModel.popOverlay() },
                onResetOnboarding = { navViewModel.resetOnboarding() },
                overlays = overlays,
            )
        }
        is AppDestination.Overlay -> {
        }
    }
}

@Composable
private fun MainContent(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    onNavigateToOverlay: (AppDestination.Overlay) -> Unit,
    onPopOverlay: () -> Unit,
    onResetOnboarding: () -> Unit,
    overlays: List<AppDestination.Overlay>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
            bottomBar = {
                if (overlays.isEmpty()) {
                    NavigationBar(
                        containerColor =
                            androidx.compose.material3.MaterialTheme.colorScheme.surface,
                        contentColor =
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    ) {
                        Tab.entries.forEach { tab ->
                            val config = tabConfigs[tab]!!
                            NavigationBarItem(
                                selected = selectedTab == tab,
                                onClick = { onTabSelected(tab) },
                                icon = { Icon(config.icon, contentDescription = config.label) },
                                label = { Text(config.label) },
                            )
                        }
                    }
                }
            },
        ) { padding ->
            when (selectedTab) {
                Tab.CheckIn -> CheckInScreen(Modifier.fillMaxSize(), padding)
                Tab.Study ->
                    StudyScreen(
                        modifier = Modifier.fillMaxSize(),
                        padding = padding,
                        onNavigateToCheckIn = { onTabSelected(Tab.CheckIn) },
                    )
                Tab.Reframe ->
                    ReframeScreen(
                        modifier = Modifier.fillMaxSize(),
                        padding = padding,
                        onNavigateToBreathing = { exerciseId ->
                            onNavigateToOverlay(AppDestination.Overlay.Breathing(exerciseId))
                        },
                        onNavigateToQuotes = { onNavigateToOverlay(AppDestination.Overlay.Quotes) },
                        onNavigateToSelfTalk = {
                            onNavigateToOverlay(AppDestination.Overlay.SelfTalk)
                        },
                        onNavigateToBrightSpots = {
                            onNavigateToOverlay(AppDestination.Overlay.BrightSpots)
                        },
                        onNavigateToInterruptedPattern = {
                            onNavigateToOverlay(AppDestination.Overlay.InterruptedPattern)
                        },
                        onNavigateToDisappearingCenter = {
                            onNavigateToOverlay(AppDestination.Overlay.DisappearingCenter)
                        },
                    )
                Tab.Analyze -> AnalyzeScreen(Modifier.fillMaxSize(), padding)
                Tab.Settings ->
                    pl.nubet.studymood.ui.screens.settings.SettingsScreen(
                        modifier = Modifier.fillMaxSize(),
                        onResetOnboarding = onResetOnboarding,
                    )
            }
        }

        overlays.forEach { overlay ->
            when (overlay) {
                is AppDestination.Overlay.Breathing -> {
                    val exerciseType = BreathingExerciseType.fromId(overlay.exerciseId)
                    if (exerciseType != null) {
                        BreathingScreen(
                            exerciseType = exerciseType,
                            onBack = onPopOverlay,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                is AppDestination.Overlay.Quotes -> {
                    val viewModel: QuotesViewModel = koinInject()
                    QuotesScreen(
                        onBackClick = onPopOverlay,
                        padding = PaddingValues(0.dp),
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is AppDestination.Overlay.SelfTalk -> {
                    pl.nubet.studymood.ui.screens.selftalk.SelfTalkScreen(
                        onBack = onPopOverlay,
                        onCheckIn = {
                            onPopOverlay()
                            onTabSelected(Tab.CheckIn)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is AppDestination.Overlay.BrightSpots -> {
                    pl.nubet.studymood.ui.screens.brightspots.BrightSpotsScreen(
                        onBack = onPopOverlay,
                        onCheckIn = {
                            onPopOverlay()
                            onTabSelected(Tab.CheckIn)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is AppDestination.Overlay.InterruptedPattern -> {
                    pl.nubet.studymood.ui.screens.interruptedpattern.InterruptedPatternScreen(
                        onBack = onPopOverlay,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is AppDestination.Overlay.DisappearingCenter -> {
                    pl.nubet.studymood.ui.screens.disappearingcenter.DisappearingCenterScreen(
                        onBack = onPopOverlay,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
