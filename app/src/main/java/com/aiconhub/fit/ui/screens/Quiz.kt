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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel

private val goals = listOf("Get fit", "Build strength", "Lose weight", "Core & flexibility")
private val levels = listOf("Beginner", "Intermediate", "Advanced")
private val limitations = listOf(
    Triple("no_jumping", "No jumping", "Skip plyometric and high impact moves."),
    Triple("easy_knees", "Easy on knees", "Avoid lunges, squats and jumps."),
    Triple("easy_wrists", "Easy on wrists", "Avoid push-ups, planks and crawls.")
)

/** The questionnaire form, shared by onboarding and the standalone quiz screen. */
@Composable
fun QuizForm(vm: AppViewModel) {
    SectionTitle("Your main goal")
    goals.chunked(2).forEach { row ->
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            row.forEach { g ->
                FilterChip(
                    selected = vm.profile.goal == g,
                    onClick = { vm.profile = vm.profile.copy(goal = g) },
                    label = { Text(g) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
    }
    Spacer(Modifier.height(8.dp))

    SectionTitle("Fitness level")
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        levels.forEachIndexed { i, l ->
            SegmentedButton(
                selected = vm.profile.level == l,
                onClick = { vm.profile = vm.profile.copy(level = l) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = levels.size)
            ) { Text(l) }
        }
    }
    Spacer(Modifier.height(8.dp))

    SectionTitle("Days per week")
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        listOf(2, 3, 4, 5).forEachIndexed { i, d ->
            SegmentedButton(
                selected = vm.profile.daysPerWeek == d,
                onClick = { vm.profile = vm.profile.copy(daysPerWeek = d) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = 4)
            ) { Text("$d") }
        }
    }
    Spacer(Modifier.height(8.dp))

    SectionTitle("Minutes per session")
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        listOf(10, 15, 20, 30).forEachIndexed { i, m ->
            SegmentedButton(
                selected = vm.profile.minutesPerDay == m,
                onClick = { vm.profile = vm.profile.copy(minutesPerDay = m) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = 4)
            ) { Text("$m") }
        }
    }
    Spacer(Modifier.height(8.dp))

    SectionTitle("Anything to work around?")
    Text(
        "Optional. Picked moves will avoid these.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(4.dp))
    limitations.forEach { (key, label, hint) ->
        val selected = vm.profile.limitations.split(",").contains(key)
        FilterChip(
            selected = selected,
            onClick = {
                val set = vm.profile.limitations.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableSet()
                if (selected) set.remove(key) else set.add(key)
                vm.profile = vm.profile.copy(limitations = set.joinToString(","))
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            hint,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )
    }
}

/** Standalone questionnaire, reachable any time from Settings or Home. */
@Composable
fun QuizScreen(vm: AppViewModel, onDone: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        BrandHeader("Your goals")
        Spacer(Modifier.height(6.dp))
        Text(
            "Answer a few questions and AiCoN Fit curates your plan and exercise picks. Everything stays on this phone.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        QuizForm(vm)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                vm.profile = vm.profile.copy(quizDone = true)
                vm.persistProfile()
                onDone()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save and curate my plan") }
        Spacer(Modifier.height(24.dp))
    }
}
