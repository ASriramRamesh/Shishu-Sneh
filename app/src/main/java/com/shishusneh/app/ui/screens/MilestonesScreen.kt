package com.shishusneh.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.model.*
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestonesScreen(vm: AppViewModel, modifier: Modifier = Modifier) {
    val statuses by vm.milestoneStatuses.collectAsState()
    val completedIds = statuses.filter { it.completed }.map { it.milestoneId }.toSet()
    val completed = completedIds.size
    var selectedCategory by remember { mutableStateOf<MilestoneCategory?>(null) }

    val categories = MilestoneCategory.values().toList()
    val filtered = ALL_MILESTONES.filter { selectedCategory == null || it.category == selectedCategory }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Milestones", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Progress
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Progress", style = MaterialTheme.typography.titleMedium)
                            Text("$completed / ${ALL_MILESTONES.size}", style = MaterialTheme.typography.headlineSmall.copy(color = Warning, fontWeight = FontWeight.Bold))
                        }
                        LinearProgressIndicator(
                            progress = { completed.toFloat() / ALL_MILESTONES.size },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = Warning,
                            trackColor = Border
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Category filter chips
                Text("Filter by category", style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary, selectedLabelColor = Color.White)
                    )
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = categoryColor(cat), selectedLabelColor = Color.White)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Group by month
            val grouped = filtered.groupBy { it.ageMonths }
            grouped.forEach { (month, milestones) ->
                item {
                    Text(
                        if (month < 2) "$month month" else "$month months",
                        style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(milestones) { milestone ->
                    val isCompleted = completedIds.contains(milestone.id)
                    MilestoneRow(
                        milestone = milestone,
                        isCompleted = isCompleted,
                        onToggle = { vm.toggleMilestone(milestone.id, !isCompleted) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MilestoneRow(milestone: Milestone, isCompleted: Boolean, onToggle: () -> Unit) {
    val catColor = categoryColor(milestone.category)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = onToggle, modifier = Modifier.size(32.dp)) {
                Icon(
                    if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle",
                    tint = if (isCompleted) Success else Border
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(milestone.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = if (isCompleted) TextSecondary else TextPrimary))
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(catColor.copy(alpha = 0.15f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(milestone.category.name.lowercase(), style = MaterialTheme.typography.bodySmall.copy(color = catColor, fontWeight = FontWeight.Medium))
                    }
                }
                Text(milestone.description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }
    }
}

private fun categoryColor(category: MilestoneCategory): Color = when (category) {
    MilestoneCategory.MOTOR    -> Primary
    MilestoneCategory.COGNITIVE -> Info
    MilestoneCategory.SOCIAL   -> Accent
    MilestoneCategory.LANGUAGE -> Success
    MilestoneCategory.SENSORY  -> Warning
}
