package com.aiconhub.fit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.AppViewModel
import com.aiconhub.fit.Screen
import com.aiconhub.fit.data.PlanRepo

@Composable
fun PlansScreen(vm: AppViewModel) {
    RootScaffold(vm, "plans") { mod ->
        LazyColumn(
            modifier = mod
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                BrandHeader("Plans")
                Spacer(Modifier.height(12.dp))
            }
            items(PlanRepo.plans.size) { i ->
                val p = PlanRepo.plans[i]
                val done = (0 until p.days.size).count { vm.planDone.contains(p.id + ":" + it) }
                val active = vm.activePlanId == p.id
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { vm.push(Screen.PlanDetail(p.id)) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row {
                            Text(p.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                            if (active) {
                                Text("Active", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${p.level} - ${p.weeks} weeks - ${p.daysPerWeek} days a week - ${if (p.equipment == "None") "No equipment" else p.equipment}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(p.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (done > 0) {
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { done / p.days.size.toFloat() },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "$done of ${p.days.size} days done",
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
