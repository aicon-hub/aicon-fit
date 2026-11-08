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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(vm: AppViewModel) {
    val pager = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(vm.profile.name) }
    var heightText by remember { mutableStateOf(if (vm.profile.heightCm > 0) vm.profile.heightCm.toInt().toString() else "") }
    var weightText by remember { mutableStateOf(if (vm.profile.weightKg > 0) vm.profile.weightKg.toInt().toString() else "") }
    var units by remember { mutableStateOf(vm.profile.units) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        HorizontalPager(state = pager, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 -> Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BrandMark(120, "AiCoN logo")
                    Spacer(Modifier.height(24.dp))
                    Text("Welcome to AiCoN Fit", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Guided workouts with animated demos that show you exactly what to do.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(20.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Text(
                            "Private by default. Everything you enter stays on this phone. No accounts, no ads, no trackers.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                1 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("About you", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Used only on this device to personalise your plan. Every field is optional.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = units == "metric",
                            onClick = { units = "metric" },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) { Text("kg, cm") }
                        SegmentedButton(
                            selected = units == "imperial",
                            onClick = { units = "imperial" },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) { Text("lb, ft") }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = heightText,
                        onValueChange = { heightText = it },
                        label = { Text(if (units == "metric") "Height (cm)" else "Height (inches)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text(if (units == "metric") "Weight (kg)" else "Weight (lb)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Make it comfortable", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(20.dp))
                    SettingSwitchRow(
                        "Reduce motion",
                        "Exercise demos stay on a single frame.",
                        vm.settings.reduceMotion
                    ) { vm.settings = vm.settings.copy(reduceMotion = it) }
                    SettingSwitchRow(
                        "High contrast",
                        "Stronger text and background contrast.",
                        vm.settings.highContrast
                    ) { vm.settings = vm.settings.copy(highContrast = it) }
                    SettingSwitchRow(
                        "Haptics",
                        "Gentle vibration on timer changes.",
                        vm.settings.haptics
                    ) { vm.settings = vm.settings.copy(haptics = it) }
                    SettingSwitchRow(
                        "Timer sounds",
                        "Short beeps during workouts.",
                        vm.settings.sound
                    ) { vm.settings = vm.settings.copy(sound = it) }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "You can change all of this later in Settings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { i ->
                val selected = pager.currentPage == i
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(if (selected) 20.dp else 8.dp)
                        .height(8.dp),
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                ) {}
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (pager.currentPage < 2) {
                TextButton(onClick = {
                    vm.finishOnboarding()
                }) { Text("Skip") }
                Spacer(Modifier.weight(1f))
                Button(onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } }) {
                    Text("Next")
                }
            } else {
                Spacer(Modifier.weight(1f))
                Button(
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        vm.profile = vm.profile.copy(
                            name = name.trim(),
                            units = units,
                            heightCm = com.aiconhub.fit.util.Units.parseHeight(heightText, units) ?: vm.profile.heightCm,
                            weightKg = com.aiconhub.fit.util.Units.parseWeight(weightText, units) ?: vm.profile.weightKg
                        )
                        vm.persistProfile()
                        vm.persistSettings()
                        vm.finishOnboarding()
                    }
                ) { Text("Start") }
            }
        }
    }
}

@Composable
fun SettingSwitchRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
