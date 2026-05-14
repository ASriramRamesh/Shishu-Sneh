package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.db.GrowthRecordEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthScreen(vm: AppViewModel, modifier: Modifier = Modifier) {
    val records by vm.growthRecords.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Growth Tracker", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add entry", tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        if (records.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.MonitorWeight, contentDescription = null, tint = Border, modifier = Modifier.size(64.dp))
                    Text("No growth records yet", style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary))
                    Text("Tap + to log your baby's first measurement", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(records) { record ->
                    GrowthRecordCard(record = record, onDelete = { vm.deleteGrowthRecord(record.id) })
                }
            }
        }
    }

    if (showDialog) {
        AddGrowthDialog(
            onDismiss = { showDialog = false },
            onSave = { weight, height, head, notes ->
                vm.addGrowthRecord(weight, height, head, notes)
                showDialog = false
            }
        )
    }
}

@Composable
private fun GrowthRecordCard(record: GrowthRecordEntity, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                Text(record.date, style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    record.weight?.let {
                        MetricChip(icon = Icons.Default.MonitorWeight, value = "${String.format("%.2f", it)} kg", color = Success)
                    }
                    record.height?.let {
                        MetricChip(icon = Icons.Default.Height, value = "${String.format("%.1f", it)} cm", color = Info)
                    }
                    record.headCircumference?.let {
                        MetricChip(icon = Icons.Default.RadioButtonUnchecked, value = "${String.format("%.1f", it)} cm", color = Warning)
                    }
                }
                if (record.notes.isNotBlank()) {
                    Text(record.notes, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Destructive.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun MetricChip(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(color = color, fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun AddGrowthDialog(onDismiss: () -> Unit, onSave: (Float?, Float?, Float?, String) -> Unit) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var head by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Growth Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = weight, onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                )
                OutlinedTextField(
                    value = height, onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                )
                OutlinedTextField(
                    value = head, onValueChange = { head = it },
                    label = { Text("Head circumference (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                )
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(weight.toFloatOrNull(), height.toFloatOrNull(), head.toFloatOrNull(), notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
