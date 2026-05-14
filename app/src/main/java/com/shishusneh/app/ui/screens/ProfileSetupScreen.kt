package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(vm: AppViewModel, onSaved: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }
    var isSaving by remember { mutableStateOf(false) }

    val genders = listOf("Boy", "Girl", "Other")

    fun validate(): Boolean {
        val e = mutableMapOf<String, String>()
        if (name.trim().length < 2) e["name"] = "Name must be at least 2 characters"
        if (dob.isBlank()) {
            e["dob"] = "Date of birth is required (YYYY-MM-DD)"
        } else {
            try {
                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).apply {
                    isLenient = false
                    parse(dob)
                }
            } catch (ex: Exception) {
                e["dob"] = "Enter date as YYYY-MM-DD (e.g. 2025-03-15)"
            }
        }
        if (gender.isBlank()) e["gender"] = "Please select a gender"
        val w = weight.toFloatOrNull()
        if (w == null || w < 0.5f || w > 7f) e["weight"] = "Enter birth weight between 0.5 and 7 kg"
        val h = height.toFloatOrNull()
        if (h == null || h < 30f || h > 60f) e["height"] = "Enter birth length between 30 and 60 cm"
        errors = e
        return e.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ChildCare, contentDescription = null, tint = Primary, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(12.dp))
            Text("Add Your Baby", style = MaterialTheme.typography.headlineMedium)
            Text("Set up your baby's profile to get started", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
        }

        // Name
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Baby's Name", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errors = errors - "name" },
                placeholder = { Text("e.g. Aarav") },
                isError = errors.containsKey("name"),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    errorBorderColor = Destructive
                )
            )
            if (errors.containsKey("name")) {
                Text(errors["name"]!!, color = Destructive, style = MaterialTheme.typography.bodySmall)
            }
        }

        // DOB
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Date of Birth", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it; errors = errors - "dob" },
                placeholder = { Text("YYYY-MM-DD") },
                isError = errors.containsKey("dob"),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    errorBorderColor = Destructive
                )
            )
            if (errors.containsKey("dob")) {
                Text(errors["dob"]!!, color = Destructive, style = MaterialTheme.typography.bodySmall)
            }
        }

        // Gender
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Gender", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                genders.forEach { g ->
                    FilterChip(
                        selected = gender == g,
                        onClick = { gender = g; errors = errors - "gender" },
                        label = { Text(g) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            if (errors.containsKey("gender")) {
                Text(errors["gender"]!!, color = Destructive, style = MaterialTheme.typography.bodySmall)
            }
        }

        // Birth weight
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Birth Weight (kg)", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it; errors = errors - "weight" },
                placeholder = { Text("e.g. 3.2") },
                isError = errors.containsKey("weight"),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    errorBorderColor = Destructive
                )
            )
            if (errors.containsKey("weight")) {
                Text(errors["weight"]!!, color = Destructive, style = MaterialTheme.typography.bodySmall)
            }
        }

        // Birth height
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Birth Length (cm)", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = height,
                onValueChange = { height = it; errors = errors - "height" },
                placeholder = { Text("e.g. 49.5") },
                isError = errors.containsKey("height"),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    errorBorderColor = Destructive
                )
            )
            if (errors.containsKey("height")) {
                Text(errors["height"]!!, color = Destructive, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (validate()) {
                    isSaving = true
                    vm.addProfile(name.trim(), dob, gender, weight.toFloat(), height.toFloat())
                    onSaved()
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (isSaving) "Saving..." else "Save & Continue", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
        }
    }
}
