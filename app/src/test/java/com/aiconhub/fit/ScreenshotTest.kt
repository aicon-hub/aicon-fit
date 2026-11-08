package com.aiconhub.fit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.aiconhub.fit.ui.FitTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-420dpi")
class ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun vm(fresh: Boolean): AppViewModel {
        val app = RuntimeEnvironment.getApplication()
        app.filesDir.listFiles()?.forEach { it.delete() }
        val vm = AppViewModel(app)
        if (!fresh) {
            vm.settings = vm.settings.copy(onboardingDone = true)
            vm.persistSettings()
            vm.goRoot(Screen.Home)
        }
        return vm
    }

    @Test
    fun onboarding() {
        val vm = vm(fresh = true)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/onboarding.png")
    }

    @Test
    fun home() {
        val vm = vm(fresh = false)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/home.png")
    }

    @Test
    fun library() {
        val vm = vm(fresh = false)
        vm.goRoot(Screen.Library)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/library.png")
    }

    @Test
    fun exerciseDetail() {
        val vm = vm(fresh = false)
        vm.push(Screen.Detail("Pushups"))
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/detail.png")
    }

    @Test
    fun plans() {
        val vm = vm(fresh = false)
        vm.goRoot(Screen.Plans)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/plans.png")
    }

    @Test
    fun planDetail() {
        val vm = vm(fresh = false)
        vm.push(Screen.PlanDetail("starter_full_body"))
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/plandetail.png")
    }

    @Test
    fun player() {
        val vm = vm(fresh = false)
        vm.startQuick("Full body A", com.aiconhub.fit.data.PlanRepo.byId("starter_full_body")!!.days[0].items)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/player.png")
    }

    @Test
    fun playerSecondExercise() {
        val vm = vm(fresh = false)
        vm.startQuick("Full body A", com.aiconhub.fit.data.PlanRepo.byId("starter_full_body")!!.days[0].items)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        // Finish all 3 sets of exercise 1, skipping each rest, land on exercise 2.
        repeat(3) {
            composeRule.onNodeWithText("Set done").performClick(); composeRule.waitForIdle()
            composeRule.onNodeWithText("Skip rest").performClick(); composeRule.waitForIdle()
        }
        composeRule.onNodeWithText("Pushups").assertIsDisplayed()
        composeRule.onNodeWithText("Exercise 2 of 4").assertIsDisplayed()
        composeRule.onRoot().captureRoboImage("build/roborazzi/player2.png")
    }

    @Test
    fun settings() {
        val vm = vm(fresh = false)
        vm.goRoot(Screen.Settings)
        composeRule.setContent { FitTheme(highContrast = false) { AppRoot(vm) } }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage("build/roborazzi/settings.png")
    }
}
