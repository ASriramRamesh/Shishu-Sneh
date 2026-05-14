package com.shishusneh.app.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.shishusneh.app.ui.screens.*
import com.shishusneh.app.viewmodel.AppViewModel

sealed class Screen(val route: String) {
    object Onboarding     : Screen("onboarding")
    object ProfileSetup   : Screen("profile_setup")
    object Main           : Screen("main")
    object AiChat         : Screen("ai_chat")
    object Nutrition      : Screen("nutrition")
    object Emergency      : Screen("emergency")
    object FeedingTracker : Screen("feeding_tracker")
    object SleepTracker   : Screen("sleep_tracker")
    object DiaperLog      : Screen("diaper_log")
    object DoctorVisits   : Screen("doctor_visits")
    object Settings       : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val vm: AppViewModel = hiltViewModel()
    val hasSeenOnboarding by vm.hasSeenOnboarding.collectAsState()
    val profiles by vm.profiles.collectAsState()
    val activeProfile by vm.activeProfile.collectAsState()

    val startDest = when {
        !hasSeenOnboarding      -> Screen.Onboarding.route
        profiles.isEmpty()      -> Screen.ProfileSetup.route
        else                    -> Screen.Main.route
    }

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    vm.markOnboardingComplete()
                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                vm = vm,
                onSaved = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
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
            AiChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Nutrition.route) {
            NutritionScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Emergency.route) {
            EmergencyScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.FeedingTracker.route) {
            FeedingTrackerScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.SleepTracker.route) {
            SleepTrackerScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.DiaperLog.route) {
            DiaperLogScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.DoctorVisits.route) {
            DoctorVisitsScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(vm = vm, onBack = { navController.popBackStack() })
        }
    }
}
