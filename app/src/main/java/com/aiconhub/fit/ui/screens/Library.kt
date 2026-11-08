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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.Screen
import com.aiconhub.fit.ui.AnimImage

@Composable
fun LibraryScreen(vm: AppViewModel) {
    var query by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("All") }
    val groups = listOf("All", "Chest", "Back", "Shoulders", "Arms", "Legs", "Core", "Glutes")
    val filtered = vm.exercises.filter { e ->
        (group == "All" || e.group == group) &&
            (query.isBlank() || e.name.contains(query, ignoreCase = true))
    }
    RootScaffold(vm, "library") { mod ->
        Column(
            modifier = mod
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            BrandHeader("Exercises")
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(groups.size) { i ->
                    FilterChip(
                        selected = group == groups[i],
                        onClick = { group = groups[i] },
                        label = { Text(groups[i]) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(filtered.size) { i ->
                    val e = filtered[i]
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { vm.push(Screen.Detail(e.id)) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimImage(
                                assetPath = e.anim,
                                reduceMotion = true,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(e.name, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    e.primaryMuscles.joinToString(", ").ifBlank { e.group },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    e.level.replaceFirstChar { it.uppercase() } + " - " + if (e.equipment == "body only") "No equipment" else e.equipment,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}
