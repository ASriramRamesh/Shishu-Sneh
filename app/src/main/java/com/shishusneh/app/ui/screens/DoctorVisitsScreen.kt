package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
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
import com.shishusneh.app.data.db.DoctorVisitEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorVisitsScreen(vm: AppViewModel, onBack: () -> Unit) {
    val visits by vm.doctorVisits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doctor Visits", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, null, tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        if (visits.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.LocalHospital, null, tint = Border, modifier = Modifier.size(64.dp))
                    Text("No visits recorded", style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary))
                    Text("Tap + to add a doctor visit", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visits) { v ->
                    VisitCard(v) { vm.deleteDoctorVisit(v.id) }
                }
            }
        }
    }

    if (showDialog) {
        AddVisitDialog(onDismiss = { showDialog = false }, onSave = { date, doc, clinic, reason, w, h, t, notes ->
            vm.addDoctorVisit(date, doc, clinic, reason, w, h, t, notes)
            showDialog = false
        })
    }
}

@Composable
private fun VisitCard(v: DoctorVisitEntity, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.LocalHospital, null, tint = Success, modifier = Modifier.size(20.dp))
                    Text(v.date, style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, null, tint = Destructive.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                }
            }
            Text(v.reason, style = MaterialTheme.typography.titleMedium)
            if (v.doctorName.isNotBlank()) {
                Text("Dr. ${v.doctorName}${if (v.clinic.isNotBlank()) " · ${v.clinic}" else ""}", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                v.weight?.let { Text("${String.format("%.2f", it)} kg", style = MaterialTheme.typography.bodySmall.copy(color = Success)) }
                v.height?.let { Text("${String.format("%.1f", it)} cm", style = MaterialTheme.typography.bodySmall.copy(color = Info)) }
                v.temperature?.let { Text("${String.format("%.1f", it)}°C", style = MaterialTheme.typography.bodySmall.copy(color = Warning)) }
            }
            if (v.notes.isNotBlank()) {
                Text(v.notes, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }
    }
}

@Composable
private fun AddVisitDialog(onDismiss: () -> Unit, onSave: (String, String, String, String, Float?, Float?, Float?, String) -> Unit) {
    var date by remember { mutableStateOf("") }
    var doctor by remember { mutableStateOf("") }
    var clinic by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var temp by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Doctor Visit") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Reason for visit") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = doctor, onValueChange = { doctor = it }, label = { Text("Doctor name") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = clinic, onValueChange = { clinic = it }, label = { Text("Clinic / Hospital") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                    OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.weight(1f), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                }
                OutlinedTextField(value = temp, onValueChange = { temp = it }, label = { Text("Temperature (°C)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
            }
        },
        confirmButton = {
            Button(onClick = { onSave(date, doctor, clinic, reason, weight.toFloatOrNull(), height.toFloatOrNull(), temp.toFloatOrNull(), notes) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
