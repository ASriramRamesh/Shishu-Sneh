package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shishusneh.app.ui.navigation.Screen
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AppViewModel

private data class MoreMenuItem(val label: String, val icon: ImageVector, val iconBg: Color, val iconTint: Color, val route: String)

private val MENU_ITEMS = listOf(
    listOf(
        MoreMenuItem("Feeding Tracker", Icons.Default.ChildFriendly, Color(0xFFFFEDE8), Accent, Screen.FeedingTracker.route),
        MoreMenuItem("Sleep Tracker", Icons.Default.Bedtime, Color(0xFFEDE9FF), Primary, Screen.SleepTracker.route),
        MoreMenuItem("Diaper Log", Icons.Default.WaterDrop, Color(0xFFE3F2FD), Info, Screen.DiaperLog.route),
        MoreMenuItem("Doctor Visits", Icons.Default.LocalHospital, Color(0xFFE8F5E9), Success, Screen.DoctorVisits.route)
    ),
    listOf(
        MoreMenuItem("AI Health Guide", Icons.Default.SmartToy, Color(0xFFE3F2FD), Info, Screen.AiChat.route),
        MoreMenuItem("Feeding & Nutrition", Icons.Default.Restaurant, Color(0xFFFFF3E0), Warning, Screen.Nutrition.route),
        MoreMenuItem("Emergency Help", Icons.Default.Emergency, Color(0xFFFFEEEE), Destructive, Screen.Emergency.route)
    ),
    listOf(
        MoreMenuItem("Settings", Icons.Default.Settings, Color(0xFFF5F5F5), TextSecondary, Screen.Settings.route)
    )
)

private val SECTION_LABELS = listOf("Tracking", "Health & Info", "App")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(vm: AppViewModel, navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("More", style = MaterialTheme.typography.titleLarge) },
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
            MENU_ITEMS.forEachIndexed { sectionIndex, items ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(SECTION_LABELS[sectionIndex], style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            items.forEachIndexed { i, item ->
                                MoreMenuRow(
                                    item = item,
                                    onClick = { navController.navigate(item.route) }
                                )
                                if (i < items.lastIndex) {
                                    HorizontalDivider(color = Border.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoreMenuRow(item: MoreMenuItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(item.iconBg, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = item.iconTint, modifier = Modifier.size(22.dp))
        }
        Text(item.label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Border, modifier = Modifier.size(20.dp))
    }
}
