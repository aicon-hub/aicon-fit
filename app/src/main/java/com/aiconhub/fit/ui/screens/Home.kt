package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.Screen
import com.aiconhub.fit.data.Curator
import com.aiconhub.fit.data.PlanRepo
import com.aiconhub.fit.data.Quotes
import com.aiconhub.fit.ui.AnimImage
import com.aiconhub.fit.ui.QuoteCard
import com.aiconhub.fit.ui.WeekChart
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(vm: AppViewModel) {
    RootScaffold(vm, "home") { mod ->
        LazyColumn(
            modifier = mod
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                BrandHeader("AiCoN Fit")
                Spacer(Modifier.height(16.dp))
                val greeting = if (vm.profile.name.isBlank()) "Ready to move?" else "Ready to move, ${vm.profile.name}?"
                Text(greeting, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("${vm.history.size}", "Workouts", Modifier.weight(1f))
                    StatCard("${vm.streakDays()}", "Day streak", Modifier.weight(1f))
                    StatCard("${vm.totalMinutes()}", "Minutes", Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("This week", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        WeekChart(vm.history, vm.todayEpochDay())
                        if (vm.history.none { it.epochDay > vm.todayEpochDay() - 7 }) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Finish a workout to light up a day.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                QuoteCard(Quotes.forDay(vm.todayEpochDay()))
                Spacer(Modifier.height(20.dp))
            }

            item {
                val active = vm.activePlanId?.let { PlanRepo.byId(it) }
                if (active != null) {
                    val next = vm.nextPlanDay(active)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Your plan", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(Modifier.height(4.dp))
                            Text(active.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(Modifier.height(8.dp))
                            if (next >= 0) {
                                Text(
                                    "Next: ${active.days[next].title}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = { vm.startPlanDay(active, next) }) { Text("Start workout") }
                            } else {
                                Text(
                                    "Plan complete. Well done.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = { vm.push(Screen.PlanDetail(active.id)) }) { Text("View plan") }
                            }
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Pick a plan", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "A guided plan tells you what to do each day.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { vm.goRoot(Screen.Plans) }) { Text("Browse plans") }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                SectionTitle("For you")
                if (!vm.profile.quizDone) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Get a plan made for you", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Answer 5 quick questions and we curate your plan and exercise picks.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { vm.push(Screen.Quiz) }) { Text("Take the quiz") }
                        }
                    }
                } else {
                    val rec = Curator.recommendedPlan(vm.profile)
                    if (rec != null && vm.activePlanId == null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Recommended for your goal", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Spacer(Modifier.height(4.dp))
                                Text(rec.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    rec.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = { vm.push(Screen.PlanDetail(rec.id)) }) { Text("View plan") }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    val picks = Curator.picks(vm.exercises, vm.profile)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(picks.size) { i ->
                            val e = picks[i]
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .width(132.dp)
                                    .clickable { vm.push(Screen.Detail(e.id)) }
                            ) {
                                Column(Modifier.padding(10.dp)) {
                                    AnimImage(
                                        assetPath = e.anim,
                                        reduceMotion = true,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(112.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(e.name, style = MaterialTheme.typography.labelLarge, maxLines = 2)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle("Recent activity")
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { vm.goRoot(Screen.Library) }) { Text("Exercise library") }
                }
                if (vm.history.isEmpty()) {
                    Text(
                        "Nothing yet. Finish a workout and it shows up here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(vm.history.take(10).size) { i ->
                val h = vm.history[i]
                val date = Instant.ofEpochSecond(h.epochDay * 86400)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEE, d MMM"))
                Card(
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
                                "${h.exerciseCount} exercises - ${h.minutes} min",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
