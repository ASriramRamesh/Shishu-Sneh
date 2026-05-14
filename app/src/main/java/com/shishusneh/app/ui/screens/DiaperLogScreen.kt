package com.shishusneh.app.ui.screens

import androidx.compose.foundation.background
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
import com.shishusneh.app.data.db.DiaperRecordEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

private data class DiaperType(val key: String, val label: String, val color: Color, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val DIAPER_TYPES = listOf(
    DiaperType("wet", "Wet", Info, Icons.Default.WaterDrop),
    DiaperType("dirty", "Dirty", Warning, Icons.Default.Circle),
    DiaperType("mixed", "Mixed", Accent, Icons.Default.BlurOn),
    DiaperType("dry", "Dry", Success, Icons.Default.CheckCircle)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaperLogScreen(vm: AppViewModel, onBack: () -> Unit) {
    val records by vm.diaperRecords.collectAsState()

    val today = remember {
        Calendar.getInstance().let { cal -> records.filter { r ->
            val rc = Calendar.getInstance().apply { timeInMillis = r.time }
            rc.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) && rc.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
        }}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diaper Log", style = MaterialTheme.typography.titleLarge) },
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
                // Today stats
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Today's Count", style = MaterialTheme.typography.titleMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            DIAPER_TYPES.forEach { dt ->
                                val count = records.count { it.type == dt.key &&
                                        Calendar.getInstance().let { cal ->
                                            val rc = Calendar.getInstance().apply { timeInMillis = it.time }
                                            rc.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) && rc.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                                        }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                    Icon(dt.icon, null, tint = dt.color, modifier = Modifier.size(20.dp))
                                    Text("$count", style = MaterialTheme.typography.headlineSmall.copy(color = dt.color, fontWeight = FontWeight.Bold))
                                    Text(dt.label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                }
                            }
                        }
                    }
                }
            }
            item {
                // Quick tap buttons
                Text("Log a Diaper Change", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DIAPER_TYPES.take(2).forEach { dt ->
                        Button(
                            onClick = { vm.addDiaperRecord(dt.key) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = dt.color),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(dt.icon, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(dt.label, style = MaterialTheme.typography.labelLarge.copy(color = Color.White))
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DIAPER_TYPES.drop(2).forEach { dt ->
                        Button(
                            onClick = { vm.addDiaperRecord(dt.key) },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = dt.color),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(dt.icon, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(dt.label, style = MaterialTheme.typography.labelLarge.copy(color = Color.White))
                        }
                    }
                }
            }
            item { Text("Recent Changes", style = MaterialTheme.typography.titleMedium) }
            if (records.isEmpty()) {
                item { Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No diaper changes logged yet", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                }}
            } else {
                items(records.take(30)) { r ->
                    DiaperRow(r) { vm.deleteDiaperRecord(r.id) }
                }
            }
        }
    }
}

@Composable
private fun DiaperRow(r: DiaperRecordEntity, onDelete: () -> Unit) {
    val dt = DIAPER_TYPES.firstOrNull { it.key == r.type } ?: DIAPER_TYPES[0]
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(dt.icon, null, tint = dt.color, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(dt.label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                Text(SimpleDateFormat("d MMM, h:mm a", Locale.getDefault()).format(Date(r.time)), style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, null, tint = Destructive.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            }
        }
    }
}
