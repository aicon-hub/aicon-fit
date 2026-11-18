package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.BuildConfig
import com.aiconhub.fit.Screen
import com.aiconhub.fit.data.ExerciseRepo
import com.aiconhub.fit.util.Units

@Composable
fun SettingsScreen(vm: AppViewModel) {
    val appContext = LocalContext.current.applicationContext
    val creditExercises = remember { ExerciseRepo.load(appContext).filter { it.videoCredit != null } }
    var name by remember(vm.profile.name) { mutableStateOf(vm.profile.name) }
    var heightText by remember(vm.profile.units) {
        mutableStateOf(
            if (vm.profile.units == "imperial")
                ((vm.profile.heightCm / 2.54).toInt()).toString()
            else vm.profile.heightCm.toInt().toString()
        )
    }
    var weightText by remember(vm.profile.units) {
        mutableStateOf(
            if (vm.profile.units == "imperial")
                ((vm.profile.weightKg * 2.20462).toInt()).toString()
            else vm.profile.weightKg.toInt().toString()
        )
    }
    var confirmClear by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        SubHeader("Settings", onBack = { vm.pop() })
        Spacer(Modifier.height(14.dp))

        SectionTitle("Appearance")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("dark" to "Dark", "black" to "Black", "light" to "Light").forEach { (key, label) ->
                FilterChip(
                    selected = vm.settings.theme == key,
                    onClick = {
                        vm.settings = vm.settings.copy(theme = key)
                        vm.persistSettings()
                    },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Black is true black for AMOLED screens.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        SettingSwitchRow("High contrast", "Stronger text and background contrast.", vm.settings.highContrast) {
            vm.settings = vm.settings.copy(highContrast = it)
            vm.persistSettings()
        }
        SettingSwitchRow("Reduce motion", "Exercise demos stay on a single frame.", vm.settings.reduceMotion) {
            vm.settings = vm.settings.copy(reduceMotion = it)
            vm.persistSettings()
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("Workout")
        SettingSwitchRow("Haptics", "Gentle vibration on timer changes.", vm.settings.haptics) {
            vm.settings = vm.settings.copy(haptics = it)
            vm.persistSettings()
        }
        SettingSwitchRow("Timer sounds", "Short beeps during workouts.", vm.settings.sound) {
            vm.settings = vm.settings.copy(sound = it)
            vm.persistSettings()
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("Profile")
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                vm.profile = vm.profile.copy(name = it.trim())
                vm.persistProfile()
            },
            label = { Text("Name") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = vm.profile.units == "metric",
                onClick = {
                    vm.profile = vm.profile.copy(units = "metric")
                    vm.persistProfile()
                },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) { Text("kg, cm") }
            SegmentedButton(
                selected = vm.profile.units == "imperial",
                onClick = {
                    vm.profile = vm.profile.copy(units = "imperial")
                    vm.persistProfile()
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) { Text("lb, ft") }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = heightText,
            onValueChange = {
                heightText = it
                Units.parseHeight(it, vm.profile.units)?.let { cm ->
                    vm.profile = vm.profile.copy(heightCm = cm)
                    vm.persistProfile()
                }
            },
            label = { Text(if (vm.profile.units == "metric") "Height (cm)" else "Height (inches)") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = weightText,
            onValueChange = {
                weightText = it
                Units.parseWeight(it, vm.profile.units)?.let { kg ->
                    vm.profile = vm.profile.copy(weightKg = kg)
                    vm.persistProfile()
                }
            },
            label = { Text(if (vm.profile.units == "metric") "Weight (kg)" else "Weight (lb)") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { vm.push(Screen.Quiz) }, modifier = Modifier.fillMaxWidth()) {
            Text("Retake the personalisation quiz")
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("Data")
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Everything is stored only on this phone. Clearing removes your profile, plans and history.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { confirmClear = true }) {
                    Text("Clear all data", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("Demo media credits")
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "75 moves: 20 filmed demos, 11 model animations and 44 photo loops. MP4 does not always mean filmed footage.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(10.dp))
                Text("The model-animation repository states MIT, but separate ownership of its CDN videos is not independently verified.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(10.dp))
                Text(
                    "The appearance of U.S. Department of War (DoW) visual information does not imply or constitute DoW endorsement.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                creditExercises.forEach { e ->
                    Spacer(Modifier.height(10.dp))
                    Text(e.name, style = MaterialTheme.typography.labelLarge)
                    Text(
                        e.videoCredit!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("About")
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("AiCoN Fit ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Free workouts, no equipment, no ads, no trackers. Made by AiCoN.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Exercise data and photo loops: Free Exercise DB (Unlicense). Filmed demos: DVIDS and Wikimedia Commons. Model MP4s: free-exercise-db-with-videos; its repository states MIT, but separate video ownership is not independently verified.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(32.dp))
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Clear all data?") },
            text = { Text("This deletes your profile, plan progress, custom workouts and history from this phone.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmClear = false
                    vm.clearAllData()
                    vm.goRoot(Screen.Onboarding)
                }) { Text("Clear everything", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Cancel") }
            }
        )
    }
}
