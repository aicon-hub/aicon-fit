package com.aiconhub.fit.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.data.PlanRepo

@Composable
fun PlanDetailScreen(vm: AppViewModel, planId: String) {
    val plan = PlanRepo.byId(planId)
    if (plan == null) {
        Column(Modifier.systemBarsPadding().padding(20.dp)) {
            Text("Plan not found.")
            TextButton(onClick = { vm.pop() }) { Text("Back") }
        }
        return
    }
    val done = (0 until plan.days.size).count { vm.planDone.contains(plan.id + ":" + it) }
    val active = vm.activePlanId == plan.id

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = { vm.pop() }) { Text("Back") }
            Text(plan.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(
                "${plan.level} - ${plan.weeks} weeks - ${plan.daysPerWeek} days a week - ${plan.equipment}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(plan.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            if (!active) {
                Button(
                    onClick = {
                        vm.resetPlan(plan.id)
                        vm.setActivePlan(plan.id)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (done > 0) "Restart this plan" else "Start this plan") }
            } else {
                Row {
                    val next = vm.nextPlanDay(plan)
                    if (next >= 0) {
                        Button(onClick = { vm.startPlanDay(plan, next) }, modifier = Modifier.weight(1f)) {
                            Text("Continue: ${plan.days[next].title}")
                        }
                    }
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    TextButton(onClick = { vm.setActivePlan(null) }) { Text("Leave plan") }
                }
            }
            Spacer(Modifier.height(16.dp))
            SectionTitle("Days")
        }
        items(plan.days.size) { i ->
            val day = plan.days[i]
            val isDone = vm.planDone.contains(plan.id + ":" + i)
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
                        Text(day.title, style = MaterialTheme.typography.titleSmall)
                        Text(
                            day.items.joinToString(" - ") { item -> vm.exercise(item.exerciseId)?.name ?: item.exerciseId },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    if (isDone) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Done",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.padding(horizontal = 4.dp))
                    }
                    TextButton(onClick = { vm.startPlanDay(plan, i) }) { Text(if (isDone) "Redo" else "Start") }
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}
