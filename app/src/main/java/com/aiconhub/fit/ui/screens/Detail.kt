package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.data.WorkoutItem
import com.aiconhub.fit.ui.DemoMedia
import com.aiconhub.fit.ui.emberBrush

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
        SubHeader(e.name, onBack = { vm.pop() })
        Spacer(Modifier.height(12.dp))
        DemoMedia(
            video = e.video,
            anim = e.anim,
            reduceMotion = vm.settings.reduceMotion,
            contentDescription = "Demonstration of ${e.name}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .clip(RoundedCornerShape(24.dp))
        )
        if (e.videoCredit != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                e.videoCredit!!,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetaChip(e.level.replaceFirstChar { it.uppercase() })
            MetaChip(e.group)
            MetaChip(if (e.equipment == "body only") "No equipment" else e.equipment.replaceFirstChar { it.uppercase() })
        }
        if (e.secondaryMuscles.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(
                "Also works: " + e.secondaryMuscles.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(18.dp))
        Text("How to do it", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                e.instructions.forEachIndexed { i, step ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(emberBrush()),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${i + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.padding(horizontal = 6.dp))
                        Text(
                            step,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        EmberButton(
            "Start this exercise",
            onClick = {
                vm.startQuick(
                    e.name,
                    listOf(WorkoutItem(e.id, sets = 3, reps = if (e.category == "strength") 10 else 0, seconds = if (e.category == "strength") 0 else 30))
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "Watch the loop once before you start. Move slowly and keep control.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
    }
}
