package com.aiconhub.fit.ui.screens

import android.app.Activity
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.HapticFeedbackConstants
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.data.WorkoutItem
import com.aiconhub.fit.ui.AnimImage
import kotlinx.coroutines.delay

private fun beep(enabled: Boolean, doubleBeep: Boolean = false) {
    if (!enabled) return
    try {
        val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
        tg.startTone(if (doubleBeep) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_BEEP, 160)
    } catch (e: Exception) {
        // Sound is best effort.
    }
}

@Composable
fun PlayerScreen(vm: AppViewModel, title: String, items: List<WorkoutItem>, planDayKey: String?) {
    if (items.isEmpty()) {
        Column(Modifier.systemBarsPadding().padding(20.dp)) {
            Text("This workout has no exercises.")
            TextButton(onClick = { vm.pop() }) { Text("Back") }
        }
        return
    }

    val view = LocalView.current
    val context = LocalContext.current
    var exIndex by remember { mutableIntStateOf(0) }
    var setNum by remember { mutableIntStateOf(1) }
    var phase by remember { mutableStateOf("work") } // work, rest, done
    var secondsLeft by remember { mutableIntStateOf(if (items[0].timed) items[0].seconds else 0) }
    var restLeft by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(true) }
    var confirmQuit by remember { mutableStateOf(false) }
    var startedAt by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val item = items[exIndex]
    val exercise = vm.exercise(item.exerciseId)

    fun buzz() {
        if (vm.settings.haptics) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        }
    }

    fun advance() {
        buzz()
        if (setNum < item.sets) {
            setNum += 1
            phase = "rest"
            restLeft = item.restSeconds
            beep(vm.settings.sound)
        } else if (exIndex < items.size - 1) {
            exIndex += 1
            setNum = 1
            phase = "rest"
            restLeft = item.restSeconds
            beep(vm.settings.sound)
        } else {
            phase = "done"
            beep(vm.settings.sound, doubleBeep = true)
            val minutes = ((System.currentTimeMillis() - startedAt) / 60000L).toInt().coerceAtLeast(1)
            vm.completeWorkout(title, minutes, items.size, planDayKey)
        }
    }

    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(phase, running, exIndex, setNum) {
        while (running && phase != "done") {
            delay(1000)
            if (phase == "work" && item.timed) {
                if (secondsLeft <= 4 && secondsLeft > 1) beep(vm.settings.sound)
                if (secondsLeft <= 1) {
                    advance()
                } else {
                    secondsLeft -= 1
                }
            } else if (phase == "rest") {
                if (restLeft <= 4 && restLeft > 1) beep(vm.settings.sound)
                if (restLeft <= 1) {
                    buzz()
                    phase = "work"
                    val nextItem = items[exIndex]
                    secondsLeft = if (nextItem.timed) nextItem.seconds else 0
                    beep(vm.settings.sound, doubleBeep = true)
                } else {
                    restLeft -= 1
                }
            } else {
                break
            }
        }
    }

    if (phase == "done") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandMark(96, "AiCoN logo")
            Spacer(Modifier.height(20.dp))
            Text("Workout done", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "$title - ${items.size} exercises",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Saved to your history.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = { vm.pop() }, modifier = Modifier.fillMaxWidth()) { Text("Done") }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { confirmQuit = true }) { Text("Quit") }
            Spacer(Modifier.weight(1f))
            Text(
                "Exercise ${exIndex + 1} of ${items.size}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        AnimImage(
            assetPath = exercise?.anim ?: "",
            reduceMotion = vm.settings.reduceMotion,
            contentDescription = "Animated demonstration of ${exercise?.name ?: "exercise"}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.height(12.dp))
        Text(exercise?.name ?: item.exerciseId, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            "Set $setNum of ${item.sets} - ${item.targetText()}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))
        if (phase == "rest") {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Rest", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(8.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { if (item.restSeconds > 0) restLeft / item.restSeconds.toFloat() else 0f },
                            modifier = Modifier.size(96.dp)
                        )
                        Text("${restLeft}s", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(Modifier.height(8.dp))
                    val nextName = if (setNum < item.sets) exercise?.name else vm.exercise(items[exIndex + 1].exerciseId)?.name
                    Text(
                        "Next: ${nextName ?: ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = {
                        phase = "work"
                        val nextItem = items[exIndex]
                        secondsLeft = if (nextItem.timed) nextItem.seconds else 0
                    }) { Text("Skip rest") }
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (item.timed) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { if (item.seconds > 0) secondsLeft / item.seconds.toFloat() else 0f },
                                modifier = Modifier.size(96.dp)
                            )
                            Text("${secondsLeft}s", style = MaterialTheme.typography.headlineMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (vm.settings.reduceMotion) "Steps are listed below." else "Follow the loop above.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            "Do ${item.reps} reps",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Move slowly and keep control. Tap the button when the set is done.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = { running = !running }) {
                            Text(if (running) "Pause" else "Resume")
                        }
                        Button(onClick = { advance() }) {
                            Text(if (item.timed) "Finish set" else "Set done")
                        }
                    }
                }
            }
        }

        if (vm.settings.reduceMotion && exercise != null && exercise.instructions.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Steps", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(6.dp))
                    exercise.instructions.forEachIndexed { i, step ->
                        Text(
                            "${i + 1}. $step",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (confirmQuit) {
        AlertDialog(
            onDismissRequest = { confirmQuit = false },
            title = { Text("Quit workout?") },
            text = { Text("Progress from this session will not be saved.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmQuit = false
                    vm.pop()
                }) { Text("Quit", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmQuit = false }) { Text("Keep going") }
            }
        )
    }
}
