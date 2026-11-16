package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.Screen
import com.aiconhub.fit.data.Curator
import com.aiconhub.fit.data.PlanRepo
import com.aiconhub.fit.data.Quotes
import com.aiconhub.fit.ui.AnimImage
import com.aiconhub.fit.ui.OrangeDeep
import com.aiconhub.fit.ui.QuoteCard
import com.aiconhub.fit.ui.rememberEntranceProgress
import com.aiconhub.fit.ui.WeekChart
import com.aiconhub.fit.ui.emberBrush
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                BrandHeader("AiCoN Fit", onSettings = { vm.push(Screen.Settings) })
                Spacer(Modifier.height(18.dp))

                val hour = LocalTime.now().hour
                val daypart = when {
                    hour < 5 -> "Up late"
                    hour < 12 -> "Good morning"
                    hour < 17 -> "Good afternoon"
                    else -> "Good evening"
                }
                val dateLine = Instant.now().atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault()))
                Text(
                    if (vm.profile.name.isBlank()) "$daypart." else "$daypart, ${vm.profile.name}.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    dateLine,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    listOf(
                        "Small steps, every day.",
                        "Your future self is watching.",
                        "No gym. No excuses. Just you.",
                        "Twenty minutes is all it takes."
                    )[(vm.todayEpochDay() % 4).toInt()],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(18.dp))
            }

            item {
                val heroIn = rememberEntranceProgress(0)
                Column(Modifier.graphicsLayer { alpha = heroIn; translationY = (1f - heroIn) * 36f }) {
                TodayHero(vm)
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("${vm.history.size}", "Workouts", Modifier.weight(1f))
                    StatCard("${vm.minutesThisWeek()}", "Min this week", Modifier.weight(1f))
                    StatCard("${vm.streakDays()}", "Day streak", Modifier.weight(1f))
                }
                Spacer(Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("This week", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                            Text(
                                "${vm.daysTrainedThisWeek()} of ${vm.profile.daysPerWeek} days",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        WeekChart(vm.history, vm.todayEpochDay())
                        if (vm.history.none { it.epochDay > vm.todayEpochDay() - 7 }) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Finish a workout to light up a day.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                }
            }

            item {
                val picksIn = rememberEntranceProgress(120)
                Column(Modifier.graphicsLayer { alpha = picksIn; translationY = (1f - picksIn) * 36f }) {
                SectionTitle("Picks for you")
                if (!vm.profile.quizDone) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("Get picks made for you", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Answer 5 quick questions and AiCoN Fit curates your plan and exercise picks.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(14.dp))
                            EmberButton("Take the 1 minute quiz", onClick = { vm.push(Screen.Quiz) }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                } else {
                    val picks = Curator.picks(vm.exercises, vm.profile)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(picks.size) { i ->
                            val e = picks[i]
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier
                                    .width(140.dp)
                                    .clickable { vm.push(Screen.Detail(e.id)) }
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    AnimImage(
                                        assetPath = e.anim,
                                        reduceMotion = vm.settings.reduceMotion,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(116.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(e.name, style = MaterialTheme.typography.labelLarge, maxLines = 2)
                                    Text(
                                        e.group,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                QuoteCard(Quotes.forDay(vm.todayEpochDay()))
                Spacer(Modifier.height(20.dp))
                }
            }

            item {
                val recentIn = rememberEntranceProgress(240)
                Column(Modifier.graphicsLayer { alpha = recentIn; translationY = (1f - recentIn) * 36f }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle("Recent activity")
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { vm.goRoot(Screen.Progress) }) { Text("See all") }
                }
                if (vm.history.isEmpty()) {
                    Text(
                        "Nothing yet. One workout and this page starts telling your story.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                }
            }

            items(vm.history.take(3).size) { i ->
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
                                "${h.exerciseCount} exercises, ${h.minutes} min",
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

/** The big gradient card that tells you what to do right now. */
@Composable
private fun TodayHero(vm: AppViewModel) {
    val active = vm.activePlanId?.let { PlanRepo.byId(it) }
    val next = active?.let { vm.nextPlanDay(it) } ?: -1
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(emberBrush())
    ) {
        Column(Modifier.padding(20.dp)) {
            when {
                active != null && next >= 0 -> {
                    Text("Your plan", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f))
                    Spacer(Modifier.height(4.dp))
                    Text(active.name, style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Next up: ${active.days[next].title} - ${active.days[next].items.size} exercises",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(16.dp))
                    HeroButton("Start workout") { vm.startPlanDay(active, next) }
                }
                active != null -> {
                    Text("Plan complete", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "You finished ${active.name}. Pick your next challenge.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(16.dp))
                    HeroButton("Browse plans") { vm.goRoot(Screen.Plans) }
                }
                else -> {
                    val rec = if (vm.profile.quizDone) Curator.recommendedPlan(vm.profile) else null
                    Text("No active plan", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        rec?.name ?: "Find your plan",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        rec?.description ?: "A guided plan tells you exactly what to do each day.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 2
                    )
                    Spacer(Modifier.height(16.dp))
                    HeroButton(if (rec != null) "View recommended plan" else "Browse plans") {
                        if (rec != null) vm.push(Screen.PlanDetail(rec.id)) else vm.goRoot(Screen.Plans)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = OrangeDeep)
    ) { Text(text, style = MaterialTheme.typography.titleSmall) }
}
