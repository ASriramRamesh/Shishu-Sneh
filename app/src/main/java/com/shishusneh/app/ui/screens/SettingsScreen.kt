package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.db.BabyProfileEntity
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: AppViewModel, onBack: () -> Unit) {
    val profiles by vm.profiles.collectAsState()
    val activeProfileId by vm.activeProfileId.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProfile by remember { mutableStateOf<BabyProfileEntity?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<BabyProfileEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Baby Profiles section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Baby Profiles", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add Profile")
                    }
                }
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column {
                        profiles.forEachIndexed { i, profile ->
                            val isActive = profile.id == activeProfileId
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { vm.setActiveProfile(profile.id) }.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(CircleShape).background(if (isActive) Primary else Border),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(profile.name.first().uppercase(), style = MaterialTheme.typography.titleMedium.copy(color = if (isActive) Color.White else TextSecondary))
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(profile.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                                    Text("DOB: ${profile.dob} · ${profile.gender}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                }
                                if (isActive) Icon(Icons.Default.CheckCircle, null, tint = Primary, modifier = Modifier.size(20.dp))
                                IconButton(onClick = { editingProfile = profile }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Edit, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                                }
                                if (profiles.size > 1) {
                                    IconButton(onClick = { showDeleteConfirm = profile }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.DeleteOutline, null, tint = Destructive.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            if (i < profiles.lastIndex) HorizontalDivider(color = Border.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

            // App info
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("About", style = MaterialTheme.typography.titleMedium)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Shishu-Sneh", style = MaterialTheme.typography.titleMedium.copy(color = Primary))
                        Text("Baby's First Year Guide", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        HorizontalDivider(color = Border.copy(alpha = 0.5f))
                        Text("Version 1.0.0", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text("All data stored locally on device. No internet required.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text("Vaccination schedule follows Indian National Immunisation Schedule (NIS).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddProfileDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, dob, gender, weight, height ->
                vm.addProfile(name, dob, gender, weight, height)
                showAddDialog = false
            }
        )
    }

    editingProfile?.let { profile ->
        EditProfileDialog(
            profile = profile,
            onDismiss = { editingProfile = null },
            onSave = { updated ->
                vm.updateProfile(updated)
                editingProfile = null
            }
        )
    }

    showDeleteConfirm?.let { profile ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Delete Profile?") },
            text = { Text("All data for ${profile.name} will be permanently deleted.") },
            confirmButton = {
                Button(onClick = { vm.deleteProfile(profile.id); showDeleteConfirm = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Destructive)) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { showDeleteConfirm = null }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun AddProfileDialog(onDismiss: () -> Unit, onSave: (String, String, String, Float, Float) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Boy") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Baby Profile") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Baby's name") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of birth (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Boy", "Girl", "Other").forEach { g ->
                        FilterChip(selected = gender == g, onClick = { gender = g }, label = { Text(g) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary, selectedLabelColor = Color.White))
                    }
                }
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Birth weight (kg)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
                OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Birth length (cm)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, dob, gender, weight.toFloatOrNull() ?: 0f, height.toFloatOrNull() ?: 0f) }, colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun EditProfileDialog(profile: BabyProfileEntity, onDismiss: () -> Unit, onSave: (BabyProfileEntity) -> Unit) {
    var name by remember { mutableStateOf(profile.name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Baby's name") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary))
        },
        confirmButton = {
            Button(onClick = { onSave(profile.copy(name = name.trim())) }, colors = ButtonDefaults.buttonColors(containerColor = Primary)) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
