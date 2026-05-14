package com.shishusneh.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shishusneh.app.ui.theme.*
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color,
    val title: String,
    val subtitle: String
)

private val PAGES = listOf(
    OnboardingPage(Icons.Default.Favorite, Color(0xFFFF8B72), Color(0xFFFFEDE8),
        "Welcome to Shishu-Sneh",
        "Your caring companion through your baby's first year. Track growth, vaccines, and milestones — all in one place."),
    OnboardingPage(Icons.Default.MonitorWeight, Primary, Color(0xFFEDE9FF),
        "Track Growth",
        "Log weight, height, and head circumference. Visualise your baby's growth on charts."),
    OnboardingPage(Icons.Default.Vaccines, Success, Color(0xFFE6F7EF),
        "Never Miss a Vaccine",
        "Complete Indian NIS vaccination schedule from birth to 18 months with due date reminders."),
    OnboardingPage(Icons.Default.EmojiEvents, Warning, Color(0xFFFFF3E0),
        "Developmental Milestones",
        "Track 26 milestones across motor, cognitive, social, language, and sensory development."),
    OnboardingPage(Icons.Default.SmartToy, Info, Color(0xFFE3F2FD),
        "AI Health Guide",
        "Ask health questions and get evidence-based guidance on fever, feeding, sleep, and more.")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState { PAGES.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip
        Box(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
            if (pagerState.currentPage < PAGES.lastIndex) {
                TextButton(onClick = onComplete, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text("Skip", color = TextSecondary, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val p = PAGES[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(p.iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(p.icon, contentDescription = null, tint = p.iconTint, modifier = Modifier.size(64.dp))
                }
                Spacer(Modifier.height(40.dp))
                Text(p.title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Text(p.subtitle, style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondary), textAlign = TextAlign.Center)
            }
        }

        // Dots + button
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot indicators
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(PAGES.size) { i ->
                    val isActive = i == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isActive) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isActive) Primary else Border)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            // CTA button
            Button(
                onClick = {
                    if (pagerState.currentPage < PAGES.lastIndex) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (pagerState.currentPage < PAGES.lastIndex) "Next" else "Get Started",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                )
            }
        }
    }
}
