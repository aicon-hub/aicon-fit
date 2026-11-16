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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.Screen
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.graphicsLayer
import com.aiconhub.fit.ui.AnimImage
import com.aiconhub.fit.ui.bounceClick
import com.aiconhub.fit.ui.rememberEntranceProgress

@Composable
fun LibraryScreen(vm: AppViewModel) {
    var query by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("All") }
    val groups = listOf("All", "Chest", "Back", "Shoulders", "Arms", "Legs", "Core", "Glutes", "Cardio")
    val filtered = vm.exercises.filter { e ->
        val groupOk = when (group) {
            "All" -> true
            "Cardio" -> e.category == "plyometrics"
            else -> e.group == group
        }
        groupOk && (query.isBlank() || e.name.contains(query, ignoreCase = true))
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
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search ${vm.exercises.size} moves") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(groups.size) { i ->
                    FilterChip(
                        selected = group == groups[i],
                        onClick = { group = groups[i] },
                        label = { Text(groups[i]) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "${filtered.size} move" + (if (filtered.size == 1) "" else "s") + ", all no-equipment",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            LazyColumn {
                items(filtered.size) { i ->
                    val e = filtered[i]
                    val enter = rememberEntranceProgress((i * 40).coerceAtMost(400))
                    val press = remember { MutableInteractionSource() }
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .graphicsLayer { alpha = enter; translationY = (1f - enter) * 30f }
                            .bounceClick(press, pressedScale = 0.97f)
                            .clickable(interactionSource = press, indication = androidx.compose.foundation.LocalIndication.current) { vm.push(Screen.Detail(e.id)) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                AnimImage(
                                    assetPath = e.anim,
                                    reduceMotion = true,
                                    contentDescription = null,
                                    modifier = Modifier.size(68.dp),
                                    crop = true
                                )
                                // Uniform ember wash so gym backdrops read as illustrations, not ads
                                Box(
                                    Modifier
                                        .size(68.dp)
                                        .background(
                                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                                listOf(
                                                    Color(0x330F0C09),
                                                    Color(0x590F0C09)
                                                )
                                            )
                                        )
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(e.name, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    e.primaryMuscles.joinToString(", ").ifBlank { e.group },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            MetaChip(e.level.replaceFirstChar { it.uppercase() })
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}
