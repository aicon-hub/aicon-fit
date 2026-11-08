package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.data.WorkoutItem
import com.aiconhub.fit.ui.AnimImage

@Composable
fun DetailScreen(vm: AppViewModel, exerciseId: String) {
    val e = vm.exercise(exerciseId)
    if (e == null) {
        Column(Modifier.systemBarsPadding().padding(20.dp)) {
            Text("Exercise not found.")
            TextButton(onClick = { vm.pop() }) { Text("Back") }
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
        TextButton(onClick = { vm.pop() }) { Text("Back") }
        AnimImage(
            assetPath = e.anim,
            reduceMotion = vm.settings.reduceMotion,
            contentDescription = "Animated demonstration of ${e.name}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.height(12.dp))
        Text(e.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip(onClick = {}, label = { Text(e.level.replaceFirstChar { it.uppercase() }) })
            SuggestionChip(onClick = {}, label = { Text(if (e.equipment == "body only") "No equipment" else e.equipment.replaceFirstChar { it.uppercase() }) })
            SuggestionChip(onClick = {}, label = { Text(e.group) })
        }
        if (e.secondaryMuscles.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                "Also works: " + e.secondaryMuscles.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(16.dp))
        Text("How to do it", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        e.instructions.forEachIndexed { i, step ->
            Row(Modifier.padding(vertical = 4.dp)) {
                Text(
                    "${i + 1}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(step, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                vm.startQuick(e.name, listOf(WorkoutItem(e.id, sets = 3, reps = if (e.category == "strength") 10 else 0, seconds = if (e.category == "strength") 0 else 30)))
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Start this exercise") }
        Spacer(Modifier.height(8.dp))
        Text(
            "Watch the loop once before you start. Move slowly and keep control.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
    }
}
