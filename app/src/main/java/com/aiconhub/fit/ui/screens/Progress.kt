package com.aiconhub.fit.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.ui.WeekChart
import com.aiconhub.fit.ui.emberBrush
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun ProgressScreen(vm: AppViewModel) {
    RootScaffold(vm, "progress") { mod ->
        LazyColumn(
            modifier = mod
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                BrandHeader("Progress")
                Spacer(Modifier.height(18.dp))

                // Streak hero
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(emberBrush())
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Current streak", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f))
                            Spacer(Modifier.height(2.dp))
                            val streakShown = remember { Animatable(0f) }
                            LaunchedEffect(Unit) {
                                streakShown.animateTo(vm.streakDays().toFloat(), tween(700))
                            }
                            Text(
                                "${streakShown.value.toInt()}",
                                style = MaterialTheme.typography.displayMedium,
                                color = Color.White
                            )
                            Text(
                                if (vm.streakDays() == 1) "day in a row" else "days in a row",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "This week",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                val today = vm.todayEpochDay()
                                val trained = vm.history.map { it.epochDay }.toSet()
                                repeat(7) { i ->
                                    val day = today - 6 + i
                                    val dotScale = remember { Animatable(0f) }
                                    LaunchedEffect(Unit) {
                                        kotlinx.coroutines.delay(80L * i)
                                        dotScale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 400f))
                                    }
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .scale(dotScale.value)
                                            .clip(CircleShape)
                                            .background(
                                                if (day in trained) Color.White
                                                else Color.White.copy(alpha = 0.35f)
                                            )
                                    )
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "${vm.daysTrainedThisWeek()} of ${vm.profile.daysPerWeek} day" + (if (vm.profile.daysPerWeek == 1) " target" else "s target"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("${vm.history.size}", "Workouts", Modifier.weight(1f))
                    StatCard("${vm.totalMinutes()}", "Total min", Modifier.weight(1f))
                    StatCard("${vm.minutesThisWeek()}", "This week", Modifier.weight(1f))
                }
                Spacer(Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Minutes, last 7 days", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(10.dp))
                        WeekChart(vm.history, vm.todayEpochDay())
                    }
                }
                Spacer(Modifier.height(14.dp))

                BmiCard(vm)
                Spacer(Modifier.height(20.dp))
            }

            item { SectionTitle("Activity history") }
            if (vm.history.isEmpty()) {
                item {
                    Text(
                        "Nothing yet. Your finished workouts will be listed here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(vm.history.size) { i ->
                val h = vm.history[i]
                val date = Instant.ofEpochSecond(h.epochDay * 86400)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEE, d MMM"))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(h.name, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "${h.exerciseCount} exercises",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${h.minutes} min", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                            Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun BmiCard(vm: AppViewModel) {
    val bmi = vm.bmi()
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {
            Text("Body mass index", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            if (bmi == null) {
                Text(
                    "Add your height and weight in Settings to see your BMI.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val (label, color) = when {
                    bmi < 18.5 -> "Underweight" to MaterialTheme.colorScheme.secondary
                    bmi < 25.0 -> "Healthy range" to MaterialTheme.colorScheme.tertiary
                    bmi < 30.0 -> "Overweight" to MaterialTheme.colorScheme.secondary
                    else -> "Obese" to MaterialTheme.colorScheme.error
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${(bmi * 10).roundToInt() / 10.0}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.weight(1f))
                    Text(label, style = MaterialTheme.typography.titleSmall, color = color)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "A rough guide only. Muscle, build and age all change what is healthy for you.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
