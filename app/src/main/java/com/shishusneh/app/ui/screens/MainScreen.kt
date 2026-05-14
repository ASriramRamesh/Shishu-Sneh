package com.shishusneh.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.shishusneh.app.viewmodel.AppViewModel

private data class TabItem(val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)

private val TABS = listOf(
    TabItem("Home",       Icons.Filled.Home,       Icons.Outlined.Home),
    TabItem("Growth",     Icons.Filled.BarChart,    Icons.Outlined.BarChart),
    TabItem("Vaccines",   Icons.Filled.Vaccines,    Icons.Outlined.Vaccines),
    TabItem("Milestones", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    TabItem("More",       Icons.Filled.GridView,    Icons.Outlined.GridView)
)

@Composable
fun MainScreen(vm: AppViewModel, navController: NavHostController) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
                TABS.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                if (selectedTab == index) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label, style = MaterialTheme.typography.labelMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = com.shishusneh.app.ui.theme.Primary,
                            selectedTextColor = com.shishusneh.app.ui.theme.Primary,
                            indicatorColor = com.shishusneh.app.ui.theme.CardBg
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeScreen(vm = vm, navController = navController, modifier = Modifier.padding(innerPadding))
            1 -> GrowthScreen(vm = vm, modifier = Modifier.padding(innerPadding))
            2 -> VaccinesScreen(vm = vm, modifier = Modifier.padding(innerPadding))
            3 -> MilestonesScreen(vm = vm, modifier = Modifier.padding(innerPadding))
            4 -> MoreScreen(vm = vm, navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
