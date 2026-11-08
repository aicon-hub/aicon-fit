package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.data.Workout
import com.aiconhub.fit.data.WorkoutItem
import java.util.UUID

@Composable
fun CustomEditScreen(vm: AppViewModel, workoutId: String?) {
    val existing = workoutId?.let { id -> vm.customWorkouts.firstOrNull { it.id == id } }
    var name by remember { mutableStateOf(existing?.name ?: "") }
    val items = remember { mutableStateListOf<WorkoutItem>().apply { existing?.items?.let { addAll(it) } } }
    var showPicker by remember { mutableStateOf(false) }
    var pickerQuery by remember { mutableStateOf("") }
    var confirmDelete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = { vm.pop() }) { Text("Back") }
        Text(if (existing == null) "New workout" else "Edit workout", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionTitle("Exercises")
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { showPicker = true }) { Text("Add") }
        }
        if (items.isEmpty()) {
            Text(
                "No exercises yet. Tap Add.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items.forEachIndexed { idx, item ->
            val ex = vm.exercise(item.exerciseId)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(ex?.name ?: item.exerciseId, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                        TextButton(onClick = { items.removeAt(idx) }) { Text("Remove") }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Sets", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Stepper(item.sets, 1, 10) { items[idx] = item.copy(sets = it) }
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                selected = !item.timed,
                                onClick = { items[idx] = item.copy(reps = if (item.reps > 0) item.reps else 10, seconds = 0) },
                                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                            ) { Text("Reps") }
                            SegmentedButton(
                                selected = item.timed,
                                onClick = { items[idx] = item.copy(seconds = if (item.seconds > 0) item.seconds else 30, reps = 0) },
                                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                            ) { Text("Time") }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (item.timed) {
                            Text("Seconds", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Stepper(item.seconds, 10, 300, 5) { items[idx] = item.copy(seconds = it) }
                        } else {
                            Text("Reps", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Stepper(item.reps, 1, 50) { items[idx] = item.copy(reps = it) }
                        }
                        Text("Rest", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Stepper(item.restSeconds, 0, 180, 5) { items[idx] = item.copy(restSeconds = it) }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val w = Workout(
                    id = existing?.id ?: UUID.randomUUID().toString(),
                    name = name.ifBlank { "My workout" },
                    items = items.toList(),
                    custom = true
                )
                vm.saveCustomWorkout(w)
                vm.pop()
            },
            enabled = items.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save workout") }
        if (existing != null) {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { confirmDelete = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Delete workout", color = MaterialTheme.colorScheme.error)
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (showPicker) {
        val results = vm.exercises.filter {
            pickerQuery.isBlank() || it.name.contains(pickerQuery, ignoreCase = true)
        }
        AlertDialog(
            onDismissRequest = { showPicker = false },
            title = { Text("Add exercise") },
            text = {
                Column {
                    OutlinedTextField(
                        value = pickerQuery,
                        onValueChange = { pickerQuery = it },
                        label = { Text("Search") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.heightIn(max = 320.dp)) {
                        items(results.size) { i ->
                            Text(
                                results[i].name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        items.add(WorkoutItem(results[i].id, sets = 3, reps = 10))
                                        showPicker = false
                                        pickerQuery = ""
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPicker = false }) { Text("Close") }
            }
        )
    }

    if (confirmDelete && existing != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete workout?") },
            text = { Text("This removes \"${existing.name}\" from your phone.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteCustomWorkout(existing.id)
                    confirmDelete = false
                    vm.pop()
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun Stepper(value: Int, min: Int, max: Int, step: Int = 1, onChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { if (value - step >= min) onChange(value - step) }) { Text("-") }
        Text("$value", style = MaterialTheme.typography.titleSmall)
        TextButton(onClick = { if (value + step <= max) onChange(value + step) }) { Text("+") }
    }
}
