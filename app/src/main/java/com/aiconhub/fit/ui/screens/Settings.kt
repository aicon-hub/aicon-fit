package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.BuildConfig
import com.aiconhub.fit.Screen
import com.aiconhub.fit.util.Units

@Composable
fun SettingsScreen(vm: AppViewModel) {
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

    RootScaffold(vm, "settings") { mod ->
        Column(
            modifier = mod
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            BrandHeader("Settings")
            Spacer(Modifier.height(16.dp))

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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            SectionTitle("Accessibility")
            SettingSwitchRow("Reduce motion", "Exercise demos stay on a single frame.", vm.settings.reduceMotion) {
                vm.settings = vm.settings.copy(reduceMotion = it)
                vm.persistSettings()
            }
            SettingSwitchRow("High contrast", "Stronger text and background contrast.", vm.settings.highContrast) {
                vm.settings = vm.settings.copy(highContrast = it)
                vm.persistSettings()
            }
            SettingSwitchRow("Haptics", "Gentle vibration on timer changes.", vm.settings.haptics) {
                vm.settings = vm.settings.copy(haptics = it)
                vm.persistSettings()
            }
            SettingSwitchRow("Timer sounds", "Short beeps during workouts.", vm.settings.sound) {
                vm.settings = vm.settings.copy(sound = it)
                vm.persistSettings()
            }
            Spacer(Modifier.height(20.dp))

            SectionTitle("Data")
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Everything is stored only on this phone. Clearing removes your profile, plans progress, history and custom workouts.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { confirmClear = true }) {
                        Text("Clear all data", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            SectionTitle("About")
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp)) {
                    Text("AiCoN Fit ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Free for everyone. No ads, no accounts, no tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Exercise data and demo images: Free Exercise DB (public domain, Unlicense). Font: Comfortaa (SIL Open Font License). Logo and brand: AiCoN.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Clear all data?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearAllData()
                    confirmClear = false
                    vm.goRoot(Screen.Onboarding)
                }) { Text("Clear", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Cancel") }
            }
        )
    }
}
