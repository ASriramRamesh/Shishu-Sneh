package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shishusneh.app.data.model.*
import com.shishusneh.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Feeding Stages", "Breastfeeding", "Daily Tips")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feeding & Nutrition", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Primary,
                edgePadding = 16.dp
            ) {
                tabs.forEachIndexed { i, t ->
                    Tab(selected = selectedTab == i, onClick = { selectedTab = i }, text = { Text(t) })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTab) {
                    0 -> FeedingStagesTab()
                    1 -> BreastfeedingTab()
                    2 -> DailyTipsTab()
                }
            }
        }
    }
}

@Composable
private fun FeedingStagesTab() {
    NUTRITION_STAGES.forEachIndexed { i, stage ->
        val colors = listOf(Color(0xFFEDE9FF), Color(0xFFFFEDE8), Color(0xFFE8F5E9), Color(0xFFFFF3E0))
        val tints = listOf(Primary, Accent, Success, Warning)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier.size(48.dp).background(colors[i], RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ChildCare, null, tint = tints[i], modifier = Modifier.size(24.dp))
                    }
                    Text(stage.ageRange, style = MaterialTheme.typography.titleMedium.copy(color = tints[i]))
                }
                Text(stage.description, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    stage.foods.forEach { food ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(tints[i], RoundedCornerShape(3.dp)))
                            Text(food, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreastfeedingTab() {
    BREASTFEEDING_TIPS.forEachIndexed { i, tip ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(32.dp).background(Color(0xFFFFEDE8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${i + 1}", style = MaterialTheme.typography.labelLarge.copy(color = Accent))
                }
                Text(tip, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DailyTipsTab() {
    DAILY_TIPS.forEach { tip ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Lightbulb, null, tint = Warning, modifier = Modifier.size(18.dp))
                    Text(tip.title, style = MaterialTheme.typography.titleMedium)
                }
                Text(tip.body, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
            }
        }
    }
}
