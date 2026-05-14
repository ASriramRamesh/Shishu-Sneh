package com.shishusneh.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Brand colours ────────────────────────────────────────────────────────────
val Primary      = Color(0xFF7C6AF7)
val PrimaryLight = Color(0xFFB8B0FF)
val Accent       = Color(0xFFFF8B72)
val Background   = Color(0xFFFFF9F5)
val Surface      = Color(0xFFFFFFFF)
val CardBg       = Color(0xFFF7F3FF)
val Success      = Color(0xFF52C989)
val Warning      = Color(0xFFFFB347)
val Info         = Color(0xFF5BB8FF)
val Destructive  = Color(0xFFFF4444)
val TextPrimary  = Color(0xFF1A1A2E)
val TextSecondary= Color(0xFF6B7280)
val Border       = Color(0xFFEDE9FF)

private val LightColorScheme = lightColorScheme(
    primary           = Primary,
    onPrimary         = Color.White,
    primaryContainer  = PrimaryLight,
    secondary         = Accent,
    onSecondary       = Color.White,
    background        = Background,
    surface           = Surface,
    onBackground      = TextPrimary,
    onSurface         = TextPrimary,
    outline           = Border,
    error             = Destructive
)

val AppTypography = Typography(
    displayLarge  = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 32.sp, lineHeight = 40.sp, color = TextPrimary),
    headlineMedium= TextStyle(fontWeight = FontWeight.Bold,   fontSize = 24.sp, lineHeight = 32.sp, color = TextPrimary),
    headlineSmall = TextStyle(fontWeight = FontWeight.SemiBold,fontSize = 20.sp,lineHeight = 28.sp, color = TextPrimary),
    titleLarge    = TextStyle(fontWeight = FontWeight.SemiBold,fontSize = 18.sp,lineHeight = 26.sp, color = TextPrimary),
    titleMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, color = TextPrimary),
    bodyLarge     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, color = TextPrimary),
    bodyMedium    = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, color = TextPrimary),
    bodySmall     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, color = TextSecondary),
    labelLarge    = TextStyle(fontWeight = FontWeight.SemiBold,fontSize = 14.sp,lineHeight = 20.sp, color = TextPrimary),
    labelMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, color = TextSecondary)
)

@Composable
fun ShishuSnehTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = AppTypography,
        content     = content
    )
}
