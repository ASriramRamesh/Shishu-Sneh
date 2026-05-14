package com.shishusneh.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.db.FeedingRecordEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedingTrackerScreen(vm: AppViewModel, onBack: () -> Unit) {
    val records by vm.feedingRecords.collectAsState()
    var selectedType by remember { mutableStateOf("breast") }
    val types = listOf("breast" to "Breastfeed", "bottle" to "Bottle", "solid" to "Solid Food")

    // Timer state for breastfeeding
    var isTimerRunning by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(0) }
    var timerStart by remember { mutableLongStateOf(0L) }
    var selectedSide by remember { mutableStateOf("Left") }
    val scope = rememberCoroutineScope()

    // Bottle/Solid form
    var amountMl by remember { mutableStateOf("") }
    var foodItem by remember { mutableStateOf("") }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000)
            timerSeconds++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feeding Tracker", style = MaterialTheme.typography.titleLarge) },
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
                // Type selector
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Feeding Type", style = MaterialTheme.typography.titleMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            types.forEach { (key, label) ->
                                FilterChip(selected = selectedType == key, onClick = { selectedType = key; isTimerRunning = false; timerSeconds = 0 },
                                    label = { Text(label) }, modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary, selectedLabelColor = Color.White))
                            }
                        }
                    }
                }
            }

            item {
                when (selectedType) {
                    "breast" -> {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color(0xFFFFEDE8)), elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text("Breastfeeding Timer", style = MaterialTheme.typography.titleMedium)
                                // Side selector
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Left", "Right", "Both").forEach { side ->
                                        FilterChip(selected = selectedSide == side, onClick = { selectedSide = side },
                                            label = { Text(side) },
                                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Accent, selectedLabelColor = Color.White))
                                    }
                                }
                                // Timer display
                                Text(formatTimer(timerSeconds), style = MaterialTheme.typography.displayLarge.copy(color = Accent, fontWeight = FontWeight.Bold))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    OutlinedButton(onClick = {
                                        isTimerRunning = false; timerSeconds = 0
                                    }) { Text("Reset") }
                                    Button(onClick = {
                                        if (!isTimerRunning) { timerStart = System.currentTimeMillis(); isTimerRunning = true }
                                        else {
                                            isTimerRunning = false
                                            vm.addFeedingRecord("breast", timerStart, System.currentTimeMillis(), timerSeconds, selectedSide, null, null, "")
                                            timerSeconds = 0
                                        }
                                    }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                                        Icon(if (isTimerRunning) Icons.Default.Stop else Icons.Default.PlayArrow, null)
                                        Spacer(Modifier.width(6.dp))
                                        Text(if (isTimerRunning) "Stop & Save" else "Start")
                                    }
                                }
                            }
                        }
                    }
                    "bottle" -> {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Bottle Feed", style = MaterialTheme.typography.titleMedium)
                                OutlinedTextField(value = amountMl, onValueChange = { amountMl = it }, label = { Text("Amount (ml)") },
                                    modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                                Button(onClick = {
                                    val now = System.currentTimeMillis()
                                    vm.addFeedingRecord("bottle", now, now, 0, null, amountMl.toFloatOrNull(), null, "")
                                    amountMl = ""
                                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                                    Text("Log Bottle Feed")
                                }
                            }
                        }
                    }
                    "solid" -> {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Solid Food", style = MaterialTheme.typography.titleMedium)
                                OutlinedTextField(value = foodItem, onValueChange = { foodItem = it }, label = { Text("Food item (e.g. mashed banana)") },
                                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                                Button(onClick = {
                                    val now = System.currentTimeMillis()
                                    vm.addFeedingRecord("solid", now, now, 0, null, null, foodItem.trim(), "")
                                    foodItem = ""
                                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Success)) {
                                    Text("Log Solid Feed")
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("Recent Feeds", style = MaterialTheme.typography.titleMedium)
            }

            if (records.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No feeds logged yet", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                    }
                }
            } else {
                items(records.take(20)) { r ->
                    FeedingRecordRow(record = r, onDelete = { vm.deleteFeedingRecord(r.id) })
                }
            }
        }
    }
}

@Composable
private fun FeedingRecordRow(record: FeedingRecordEntity, onDelete: () -> Unit) {
    val (icon, color, label) = when (record.type) {
        "breast" -> Triple(Icons.Default.ChildFriendly, Accent, "Breastfeed${record.side?.let { " · $it" } ?: ""}")
        "bottle" -> Triple(Icons.Default.LocalDrink, Info, "Bottle${record.amountMl?.let { " · ${it.toInt()} ml" } ?: ""}")
        else     -> Triple(Icons.Default.Restaurant, Success, "Solid${record.foodItem?.let { " · $it" } ?: ""}")
    }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()).format(Date(record.startTime)), style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    if (record.durationSeconds > 0) {
                        Text("· ${formatTimer(record.durationSeconds)}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, null, tint = Destructive.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

private fun formatTimer(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}
