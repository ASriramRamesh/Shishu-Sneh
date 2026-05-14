package com.shishusneh.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.db.SleepRecordEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTrackerScreen(vm: AppViewModel, onBack: () -> Unit) {
    val records by vm.sleepRecords.collectAsState()
    var isRunning by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }
    var startTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        while (isRunning) { delay(1000); seconds++ }
    }

    val todaySleep = remember(records) {
        val cal = Calendar.getInstance()
        records.filter { r ->
            val rc = Calendar.getInstance().apply { timeInMillis = r.startTime }
            rc.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) &&
                    rc.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
        }.sumOf { it.durationSeconds }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sleep Tracker", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Today's total
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color(0xFFEDE9FF)), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(Icons.Default.Bedtime, null, tint = Primary, modifier = Modifier.size(40.dp))
                        Column {
                            Text("Today's Sleep", style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                            Text(formatSleep(todaySleep), style = MaterialTheme.typography.headlineMedium.copy(color = Primary, fontWeight = FontWeight.Bold))
                            Text("Recommended: 14–17 hrs for newborns", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
            item {
                // Timer card
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Sleep Timer", style = MaterialTheme.typography.titleMedium)
                        Text(formatSleep(seconds), style = MaterialTheme.typography.displayLarge.copy(color = Primary, fontWeight = FontWeight.Bold))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = { isRunning = false; seconds = 0 }) { Text("Reset") }
                            Button(
                                onClick = {
                                    if (!isRunning) { startTime = System.currentTimeMillis(); isRunning = true }
                                    else { isRunning = false; vm.addSleepRecord(startTime, System.currentTimeMillis(), seconds, ""); seconds = 0 }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary)
                            ) {
                                Icon(if (isRunning) Icons.Default.Stop else Icons.Default.Bedtime, null)
                                Spacer(Modifier.width(6.dp))
                                Text(if (isRunning) "Stop & Save" else "Start Sleep")
                            }
                        }
                    }
                }
            }
            item { Text("Sleep History", style = MaterialTheme.typography.titleMedium) }
            if (records.isEmpty()) {
                item { Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No sleep sessions yet", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                }}
            } else {
                items(records.take(20)) { r ->
                    SleepRow(r) { vm.deleteSleepRecord(r.id) }
                }
            }
        }
    }
}

@Composable
private fun SleepRow(r: SleepRecordEntity, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.Bedtime, null, tint = Primary, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(formatSleep(r.durationSeconds), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()).format(Date(r.startTime)), style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, null, tint = Destructive.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

private fun formatSleep(seconds: Int): String {
    val h = seconds / 3600; val m = (seconds % 3600) / 60
    return when { h > 0 && m > 0 -> "${h}h ${m}m"; h > 0 -> "${h}h"; else -> "${m}m" }
}
