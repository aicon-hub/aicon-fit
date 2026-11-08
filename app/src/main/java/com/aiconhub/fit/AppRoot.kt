package com.aiconhub.fit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiconhub.fit.ui.screens.CustomEditScreen
import com.aiconhub.fit.ui.screens.CustomScreen
import com.aiconhub.fit.ui.screens.DetailScreen
import com.aiconhub.fit.ui.screens.HomeScreen
import com.aiconhub.fit.ui.screens.LibraryScreen
import com.aiconhub.fit.ui.screens.OnboardingScreen
import com.aiconhub.fit.ui.screens.PlanDetailScreen
import com.aiconhub.fit.ui.screens.PlansScreen
import com.aiconhub.fit.ui.screens.PlayerScreen
import com.aiconhub.fit.ui.screens.SettingsScreen

@Composable
fun AppRoot(vm: AppViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when (val s = vm.stack.last()) {
            is Screen.Onboarding -> OnboardingScreen(vm)
            is Screen.Home -> HomeScreen(vm)
            is Screen.Library -> LibraryScreen(vm)
            is Screen.Detail -> DetailScreen(vm, s.exerciseId)
            is Screen.Plans -> PlansScreen(vm)
            is Screen.PlanDetail -> PlanDetailScreen(vm, s.planId)
            is Screen.Custom -> CustomScreen(vm)
            is Screen.CustomEdit -> CustomEditScreen(vm, s.workoutId)
            is Screen.Settings -> SettingsScreen(vm)
            is Screen.Player -> PlayerScreen(vm, s.title, s.items, s.planDayKey)
        }
    }
    BackHandler(enabled = vm.stack.size > 1) { vm.pop() }
}
