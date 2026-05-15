package com.shishusneh.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shishusneh.app.ui.screens.AiChatScreen
import com.shishusneh.app.ui.screens.DiaperLogScreen
import com.shishusneh.app.ui.screens.DoctorVisitsScreen
import com.shishusneh.app.ui.screens.EmergencyScreen
import com.shishusneh.app.ui.screens.FeedingTrackerScreen
import com.shishusneh.app.ui.screens.MainScreen
import com.shishusneh.app.ui.screens.NutritionScreen
import com.shishusneh.app.ui.screens.OnboardingScreen
import com.shishusneh.app.ui.screens.ProfileSetupScreen
import com.shishusneh.app.ui.screens.SettingsScreen
import com.shishusneh.app.ui.screens.SleepTrackerScreen
import com.shishusneh.app.viewmodel.AppViewModel

// App navigation destinations
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object ProfileSetup : Screen("profile_setup")
    object Main : Screen("main")
    object AiChat : Screen("ai_chat")
    object Nutrition : Screen("nutrition")
    object Emergency : Screen("emergency")
    object FeedingTracker : Screen("feeding_tracker")
    object SleepTracker : Screen("sleep_tracker")
    object DiaperLog : Screen("diaper_log")
    object DoctorVisits : Screen("doctor_visits")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val vm: AppViewModel = hiltViewModel()

    val hasSeenOnboarding by vm.hasSeenOnboarding.collectAsState()
    val profiles by vm.profiles.collectAsState()
    val activeProfile by vm.activeProfile.collectAsState()

    // Shared back navigation callback
    val goBack = {
        navController.popBackStack()
    }

    // Determine app start destination
    val startDest = when {
        !hasSeenOnboarding -> Screen.Onboarding.route
        profiles.isEmpty() -> Screen.ProfileSetup.route
        else -> Screen.Main.route
    }

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {

        composable(Screen.Onboarding.route) {

            OnboardingScreen(
                onComplete = {

                    vm.markOnboardingComplete()

                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.ProfileSetup.route) {

            ProfileSetupScreen(
                vm = vm,
                onSaved = {

                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.ProfileSetup.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Main.route) {

            MainScreen(
                vm = vm,
                navController = navController
            )
        }

        composable(Screen.AiChat.route) {

            AiChatScreen(
                onBack = goBack
            )
        }

        composable(Screen.Nutrition.route) {

            NutritionScreen(
                onBack = goBack
            )
        }

        composable(Screen.Emergency.route) {

            EmergencyScreen(
                onBack = goBack
            )
        }

        composable(Screen.FeedingTracker.route) {

            FeedingTrackerScreen(
                vm = vm,
                onBack = goBack
            )
        }

        composable(Screen.SleepTracker.route) {

            SleepTrackerScreen(
                vm = vm,
                onBack = goBack
            )
        }

        composable(Screen.DiaperLog.route) {

            DiaperLogScreen(
                vm = vm,
                onBack = goBack
            )
        }

        composable(Screen.DoctorVisits.route) {

            DoctorVisitsScreen(
                vm = vm,
                onBack = goBack
            )
        }

        composable(Screen.Settings.route) {

            SettingsScreen(
                vm = vm,
                onBack = goBack
            )
        }
    }
}
