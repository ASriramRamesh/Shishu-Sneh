package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shishusneh.app.data.model.*
import com.shishusneh.app.ui.navigation.Screen
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(vm: AppViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
    val profile by vm.activeProfile.collectAsState()
    val vaccineStatuses by vm.vaccineStatuses.collectAsState()
    val growthRecords by vm.growthRecords.collectAsState()
    val milestoneStatuses by vm.milestoneStatuses.collectAsState()

    val ageMonths = vm.getBabyAgeMonths()
    val ageText = when {
        ageMonths < 1 -> "Newborn"
        ageMonths == 1 -> "1 month old"
        else -> "$ageMonths months old"
    }

    val nextVaccine = remember(vaccineStatuses) {
        val completedIds = vaccineStatuses.filter { it.completed }.map { it.vaccineId }.toSet()
        val dobMillis = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(profile?.dob ?: "")?.time ?: 0L
        } catch (e: Exception) { 0L }
        generateVaccineSchedule(dobMillis)
            .firstOrNull { (v, _) -> !completedIds.contains(v.id) }
    }

    val daysUntilNextVaccine = nextVaccine?.let { (_, dueMs) ->
        val diff = dueMs - System.currentTimeMillis()
        (diff / (1000 * 60 * 60 * 24)).toInt()
    }

    val completedMilestones = milestoneStatuses.count { it.completed }
    val lastWeight = growthRecords.firstOrNull()?.weight

    val tip = remember { DAILY_TIPS.random() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Primary, Color(0xFF9B8FFF))))
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Column {
                Text("Good ${greeting()}", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))
                Spacer(Modifier.height(4.dp))
                Text(
                    profile?.name ?: "Your Baby",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Text(ageText, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f)))
            }
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Quick stats row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Vaccines,
                    iconColor = Info,
                    label = "Next Vaccine",
                    value = daysUntilNextVaccine?.let {
                        when {
                            it < 0 -> "Overdue"
                            it == 0 -> "Today"
                            else -> "$it days"
                        }
                    } ?: "All done",
                    valueColor = when {
                        daysUntilNextVaccine == null -> Success
                        daysUntilNextVaccine < 0 -> Destructive
                        daysUntilNextVaccine <= 7 -> Warning
                        else -> TextPrimary
                    }
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.MonitorWeight,
                    iconColor = Success,
                    label = "Last Weight",
                    value = lastWeight?.let { "${String.format("%.1f", it)} kg" } ?: "Not logged"
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.EmojiEvents,
                    iconColor = Warning,
                    label = "Milestones",
                    value = "$completedMilestones / ${ALL_MILESTONES.size}"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ChildCare,
                    iconColor = Primary,
                    label = "Age",
                    value = ageText
                )
            }

            // Daily tip card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Warning, modifier = Modifier.size(20.dp))
                        Text("Today's Tip", style = MaterialTheme.typography.labelLarge.copy(color = Warning))
                    }
                    Text(tip.title, style = MaterialTheme.typography.titleMedium)
                    Text(tip.body, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }

            // Next vaccine card
            nextVaccine?.let { (vaccine, dueMs) ->
                val overdue = System.currentTimeMillis() > dueMs
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (overdue) Color(0xFFFFEEEE) else Color(0xFFE8F5E9))
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                                .background(if (overdue) Destructive else Success),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Vaccines, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Column(Modifier.weight(1f)) {
                            Text(vaccine.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (overdue) "Overdue — book appointment now"
                                else "Due ${SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(dueMs))}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (overdue) Destructive else Success
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(value, style = MaterialTheme.typography.titleMedium.copy(color = valueColor, fontWeight = FontWeight.Bold))
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun greeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "morning"
        hour < 17 -> "afternoon"
        else       -> "evening"
    }
}
