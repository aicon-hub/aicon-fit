package com.aiconhub.fit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aiconhub.fit.ui.screens.CustomEditScreen
import com.aiconhub.fit.ui.screens.DetailScreen
import com.aiconhub.fit.ui.screens.HomeScreen
import com.aiconhub.fit.ui.screens.LibraryScreen
import com.aiconhub.fit.ui.screens.OnboardingScreen
import com.aiconhub.fit.ui.screens.PlanDetailScreen
import com.aiconhub.fit.ui.screens.PlansScreen
import com.aiconhub.fit.ui.screens.PlayerScreen
import com.aiconhub.fit.ui.screens.ProgressScreen
import com.aiconhub.fit.ui.screens.QuizScreen
import com.aiconhub.fit.ui.screens.SettingsScreen
import com.aiconhub.fit.ui.screens.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppRoot(vm: AppViewModel, showSplash: Boolean = true) {
    var booted by remember { mutableStateOf(!showSplash) }
    LaunchedEffect(Unit) {
        if (showSplash) {
            delay(if (vm.settings.reduceMotion) 250L else 1100L)
            booted = true
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        if (!booted) {
            SplashScreen()
        } else {
            Crossfade(targetState = vm.stack.last(), label = "screen") { s ->
                when (s) {
                    is Screen.Onboarding -> OnboardingScreen(vm)
                    is Screen.Home -> HomeScreen(vm)
                    is Screen.Library -> LibraryScreen(vm)
                    is Screen.Detail -> DetailScreen(vm, s.exerciseId)
                    is Screen.Plans -> PlansScreen(vm)
                    is Screen.PlanDetail -> PlanDetailScreen(vm, s.planId)
                    is Screen.Progress -> ProgressScreen(vm)
                    is Screen.CustomEdit -> CustomEditScreen(vm, s.workoutId)
                    is Screen.Quiz -> QuizScreen(vm) { vm.pop() }
                    is Screen.Settings -> SettingsScreen(vm)
                    is Screen.Player -> PlayerScreen(vm, s.title, s.items, s.planDayKey)
                }
            }
        }
    }
    BackHandler(enabled = booted && vm.stack.size > 1) { vm.pop() }
}
