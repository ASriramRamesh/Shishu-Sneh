package com.shishusneh.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shishusneh.app.ui.theme.*

private data class EmergencyNumber(val name: String, val number: String, val desc: String)
private data class FirstAidGuide(val title: String, val steps: List<String>, val warning: String? = null)

private val EMERGENCY_NUMBERS = listOf(
    EmergencyNumber("National Emergency", "112", "Police, Fire & Ambulance"),
    EmergencyNumber("Ambulance (CATS)", "108", "Free ambulance service"),
    EmergencyNumber("Child Helpline", "1098", "CHILDLINE – 24x7 child protection"),
    EmergencyNumber("Health Helpline", "1800-180-1104", "Toll-free health advisory")
)

private val FIRST_AID_GUIDES = listOf(
    FirstAidGuide(
        "Choking Baby (under 1 year)",
        listOf(
            "Hold baby face-down on your forearm, head lower than chest",
            "Give 5 firm back blows between shoulder blades with heel of hand",
            "Turn baby face-up on your other arm",
            "Give 5 chest thrusts — 2 fingers on centre of chest, just below nipple line",
            "Alternate back blows and chest thrusts",
            "If baby becomes unconscious, call 112 immediately and start infant CPR"
        ),
        "Do NOT do abdominal thrusts (Heimlich) on babies under 1 year"
    ),
    FirstAidGuide(
        "High Fever (above 38°C)",
        listOf(
            "Remove excess clothing — keep baby comfortable",
            "Give paracetamol drops (as prescribed) — never aspirin",
            "Sponge with lukewarm water (not cold or icy)",
            "Ensure baby drinks frequently (breast milk or formula)",
            "Monitor temperature every 30 minutes",
            "Seek immediate care if fever exceeds 39°C, baby is under 3 months, has seizures, difficulty breathing, or rash"
        )
    ),
    FirstAidGuide(
        "Infant CPR (if baby is unresponsive)",
        listOf(
            "Check for response — tap foot, shout baby's name",
            "Call 112 immediately or ask someone to call while you start CPR",
            "Tilt head slightly back to open airway",
            "Give 2 rescue breaths — cover baby's mouth AND nose with your mouth, give gentle puffs",
            "Do 30 chest compressions — 2 fingers on centre of chest, depth 4 cm, rate 100–120/min",
            "Continue 30:2 ratio until help arrives or baby recovers"
        ),
        "Only give breaths if you are trained; otherwise continuous chest compressions"
    ),
    FirstAidGuide(
        "Severe Allergic Reaction",
        listOf(
            "Signs: swelling of lips/face/throat, hives, difficulty breathing, vomiting",
            "Call 112 immediately",
            "Keep baby as still and calm as possible",
            "If prescribed, administer epinephrine auto-injector immediately",
            "Do not give food or water",
            "Lay baby flat with legs elevated unless breathing is difficult"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var expandedGuide by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Help", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFEEEE))
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Emergency banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEEEE))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Warning, null, tint = Destructive, modifier = Modifier.size(32.dp))
                    Text(
                        "In a life-threatening emergency, call 112 immediately. Do not wait.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Destructive, fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            // Emergency numbers
            Text("Emergency Numbers", style = MaterialTheme.typography.titleMedium)
            EMERGENCY_NUMBERS.forEach { en ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(en.name, style = MaterialTheme.typography.titleMedium)
                            Text(en.desc, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${en.number}"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Destructive),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Call, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(en.number, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            // First aid guides
            Text("First Aid Guides", style = MaterialTheme.typography.titleMedium)
            FIRST_AID_GUIDES.forEachIndexed { i, guide ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { expandedGuide = if (expandedGuide == i) null else i },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(Icons.Default.MedicalServices, null, tint = Destructive, modifier = Modifier.size(20.dp))
                                Text(guide.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            }
                            Icon(if (expandedGuide == i) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = TextSecondary)
                        }
                        if (expandedGuide == i) {
                            Spacer(Modifier.height(12.dp))
                            guide.steps.forEachIndexed { step, text ->
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(vertical = 3.dp)) {
                                    Text("${step + 1}.", style = MaterialTheme.typography.labelLarge.copy(color = Destructive))
                                    Text(text, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            guide.warning?.let {
                                Spacer(Modifier.height(8.dp))
                                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), shape = RoundedCornerShape(8.dp)) {
                                    Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.Warning, null, tint = Warning, modifier = Modifier.size(16.dp))
                                        Text(it, style = MaterialTheme.typography.bodySmall.copy(color = Warning))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
