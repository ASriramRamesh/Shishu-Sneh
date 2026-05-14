package com.shishusneh.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.shishusneh.app.data.model.Vaccine
import com.shishusneh.app.data.model.generateVaccineSchedule
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinesScreen(vm: AppViewModel, modifier: Modifier = Modifier) {
    val profile by vm.activeProfile.collectAsState()
    val statuses by vm.vaccineStatuses.collectAsState()

    val schedule = remember(profile?.dob) {
        val dobMillis = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(profile?.dob ?: "")?.time ?: 0L
        } catch (e: Exception) { 0L }
        generateVaccineSchedule(dobMillis)
    }

    val completedIds = statuses.filter { it.completed }.map { it.vaccineId }.toSet()
    val completed = schedule.count { (v, _) -> completedIds.contains(v.id) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Vaccinations", style = MaterialTheme.typography.titleLarge) },
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
                // Progress header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Vaccination Progress", style = MaterialTheme.typography.titleMedium)
                            Text("$completed / ${schedule.size}", style = MaterialTheme.typography.headlineSmall.copy(color = Primary, fontWeight = FontWeight.Bold))
                        }
                        LinearProgressIndicator(
                            progress = { if (schedule.isEmpty()) 0f else completed.toFloat() / schedule.size },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = Primary,
                            trackColor = Border
                        )
                        Text("Indian National Immunisation Schedule (NIS)", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Group by month
            val grouped = schedule.groupBy { (v, _) -> v.ageMonths }
            grouped.forEach { (month, vaccines) ->
                item {
                    Text(
                        if (month == 0) "At Birth" else if (month < 2) "$month Month" else "$month Months",
                        style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(vaccines) { (vaccine, dueMs) ->
                    val isCompleted = completedIds.contains(vaccine.id)
                    val isOverdue = !isCompleted && System.currentTimeMillis() > dueMs
                    VaccineRow(
                        vaccine = vaccine,
                        dueMs = dueMs,
                        isCompleted = isCompleted,
                        isOverdue = isOverdue,
                        onToggle = { vm.toggleVaccine(vaccine.id, !isCompleted) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VaccineRow(
    vaccine: Vaccine,
    dueMs: Long,
    isCompleted: Boolean,
    isOverdue: Boolean,
    onToggle: () -> Unit
) {
    val bgColor = when {
        isCompleted -> Color(0xFFE8F5E9)
        isOverdue   -> Color(0xFFFFEEEE)
        else        -> Color.White
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(32.dp)) {
                Icon(
                    if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle",
                    tint = when { isCompleted -> Success; isOverdue -> Destructive; else -> Border }
                )
            }
            Column(Modifier.weight(1f)) {
                Text(vaccine.name, style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCompleted) TextSecondary else TextPrimary
                ))
                Text(
                    "Due: ${SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(dueMs))}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = when { isCompleted -> Success; isOverdue -> Destructive; else -> TextSecondary }
                    )
                )
                Text(vaccine.diseases.joinToString(", "), style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary.copy(alpha = 0.7f)))
            }
        }
    }
}
