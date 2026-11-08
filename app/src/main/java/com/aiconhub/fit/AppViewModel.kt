package com.aiconhub.fit

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.aiconhub.fit.data.AppSettings
import com.aiconhub.fit.data.ExerciseRepo
import com.aiconhub.fit.data.HistoryEntry
import com.aiconhub.fit.data.Plan
import com.aiconhub.fit.data.PlanRepo
import com.aiconhub.fit.data.Profile
import com.aiconhub.fit.data.Store
import com.aiconhub.fit.data.Workout
import com.aiconhub.fit.data.WorkoutItem
import com.aiconhub.fit.util.Stats
import java.time.LocalDate

sealed interface Screen {
    data object Onboarding : Screen
    data object Home : Screen
    data object Library : Screen
    data class Detail(val exerciseId: String) : Screen
    data object Plans : Screen
    data class PlanDetail(val planId: String) : Screen
    data object Custom : Screen
    data class CustomEdit(val workoutId: String?) : Screen
    data object Settings : Screen
    data class Player(val title: String, val items: List<WorkoutItem>, val planDayKey: String?) : Screen
}

class AppViewModel(app: Application) : AndroidViewModel(app) {
    val store = Store(app)
    val exercises = ExerciseRepo.load(app)

    var profile by mutableStateOf(store.profile.copy())
    var settings by mutableStateOf(store.settings.copy())
    var history by mutableStateOf(store.history.toList())
    var planDone by mutableStateOf(store.planDone.toSet())
    var activePlanId by mutableStateOf(store.activePlanId)
    var customWorkouts by mutableStateOf(store.customWorkouts.toList())

    val stack = mutableStateListOf<Screen>()

    init {
        stack.add(if (settings.onboardingDone) Screen.Home else Screen.Onboarding)
    }

    fun push(s: Screen) = stack.add(s)
    fun pop() {
        if (stack.size > 1) stack.removeAt(stack.size - 1)
    }
    fun goRoot(s: Screen) {
        stack.clear()
        stack.add(s)
    }

    fun persistProfile() {
        store.profile = profile.copy()
        store.save()
    }

    fun persistSettings() {
        store.settings = settings.copy()
        store.save()
    }

    fun finishOnboarding() {
        settings = settings.copy(onboardingDone = true)
        persistSettings()
        goRoot(Screen.Home)
    }

    fun exercise(id: String) = ExerciseRepo.byId(getApplication(), id)

    fun plan(id: String): Plan? = PlanRepo.byId(id)

    fun nextPlanDay(plan: Plan): Int = (0 until plan.days.size).firstOrNull { !planDone.contains(plan.id + ":" + it) } ?: -1

    fun startPlanDay(plan: Plan, dayIndex: Int) {
        val day = plan.days[dayIndex]
        push(Screen.Player(plan.name + " - " + day.title, day.items, plan.id + ":" + dayIndex))
    }

    fun startQuick(title: String, items: List<WorkoutItem>) {
        push(Screen.Player(title, items, null))
    }

    fun completeWorkout(title: String, minutes: Int, exerciseCount: Int, planDayKey: String?) {
        val today = LocalDate.now().toEpochDay()
        store.addHistory(HistoryEntry(title, today, minutes, exerciseCount))
        if (planDayKey != null) {
            store.planDone.add(planDayKey)
            store.save()
            planDone = store.planDone.toSet()
        }
        history = store.history.toList()
    }

    fun markPlanDayUndone(planDayKey: String) {
        store.planDone.remove(planDayKey)
        store.save()
        planDone = store.planDone.toSet()
    }

    fun setActivePlan(planId: String?) {
        store.activePlanId = planId
        store.save()
        activePlanId = planId
    }

    fun resetPlan(planId: String) {
        store.planDone.removeIf { it.startsWith(planId + ":") }
        store.save()
        planDone = store.planDone.toSet()
    }

    fun saveCustomWorkout(workout: Workout) {
        store.customWorkouts.removeIf { it.id == workout.id }
        store.customWorkouts.add(workout)
        store.save()
        customWorkouts = store.customWorkouts.toList()
    }

    fun deleteCustomWorkout(id: String) {
        store.customWorkouts.removeIf { it.id == id }
        store.save()
        customWorkouts = store.customWorkouts.toList()
    }

    fun clearAllData() {
        store.clearAll()
        profile = store.profile.copy()
        settings = store.settings.copy()
        history = emptyList()
        planDone = emptySet()
        activePlanId = null
        customWorkouts = emptyList()
    }

    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()

    fun streakDays(): Int = Stats.streak(history.map { it.epochDay }.toSet(), todayEpochDay())

    fun totalMinutes(): Int = history.sumOf { it.minutes }
}
