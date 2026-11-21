package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// ---------------------------------------------------------------------------
// RUPP Pay design tokens: RUPP school green (forest + leaf green) with a royal gold accent.
// ---------------------------------------------------------------------------
object RuppPalette {
    val Forest900 = Color(0xFF0A2A1E)
    val Forest800 = Color(0xFF123D2C)
    val Forest700 = Color(0xFF17513A)
    val Forest600 = Color(0xFF1E6B4A)
    val Forest500 = Color(0xFF2E8B5E)
    val Forest200 = Color(0xFFBFDCCB)
    val Forest100 = Color(0xFFE2F1E7)

    val Green700 = Color(0xFF15803D)
    val Green600 = Color(0xFF16A34A)
    val Green500 = Color(0xFF22C55E)
    val Green400 = Color(0xFF4ADE80)
    val Green100 = Color(0xFFDCFCE7)

    val Gold = Color(0xFFE8B931)
    val GoldSoft = Color(0xFFFFF5D6)

    val Amber = Color(0xFFF59E0B)
    val Blue = Color(0xFF3B82F6)
    val Red = Color(0xFFEF4444)
    val Slate = Color(0xFF64748B)
}

/** Brand colors that Material's ColorScheme has no slot for. */
@Immutable
data class RuppColors(
    val hero: Brush,
    val heroSolid: Color,
    val cta: Brush,
    val success: Color,
    val successBg: Color,
    val warning: Color,
    val warningBg: Color,
    val info: Color,
    val infoBg: Color,
    val danger: Color,
    val dangerBg: Color,
    val neutral: Color,
    val neutralBg: Color,
    val gold: Color,
    val goldBg: Color,
    val muted: Color,
    val cardBorder: Color,
    val skeleton: Color,
    val isDark: Boolean
)

private val LightRupp = RuppColors(
    hero = Brush.linearGradient(listOf(RuppPalette.Forest800, RuppPalette.Forest700, RuppPalette.Forest600)),
    heroSolid = RuppPalette.Forest800,
    cta = Brush.horizontalGradient(listOf(RuppPalette.Forest800, RuppPalette.Forest600)),
    success = RuppPalette.Green700, successBg = Color(0xFFE3F5E9),
    warning = Color(0xFFB45309), warningBg = Color(0xFFFFF3D6),
    info = Color(0xFF1D4ED8), infoBg = Color(0xFFE3ECFF),
    danger = Color(0xFFDC2626), dangerBg = Color(0xFFFDE7E7),
    neutral = RuppPalette.Slate, neutralBg = Color(0xFFEEF0EC),
    gold = Color(0xFFA97C00), goldBg = RuppPalette.GoldSoft,
    muted = Color(0xFF5F6B63),
    cardBorder = Color(0xFFE4E8E2),
    skeleton = Color(0xFFE7EBE5),
    isDark = false
)

private val DarkRupp = RuppColors(
    hero = Brush.linearGradient(listOf(Color(0xFF0B2219), Color(0xFF103626), Color(0xFF17513A))),
    heroSolid = Color(0xFF0B2219),
    cta = Brush.horizontalGradient(listOf(RuppPalette.Forest600, RuppPalette.Forest500)),
    success = RuppPalette.Green400, successBg = Color(0xFF0F2E1E),
    warning = Color(0xFFFBBF24), warningBg = Color(0xFF33270B),
    info = Color(0xFF93B4FF), infoBg = Color(0xFF16264A),
    danger = Color(0xFFF87171), dangerBg = Color(0xFF3A1517),
    neutral = Color(0xFF9AA89F), neutralBg = Color(0xFF1A241F),
    gold = RuppPalette.Gold, goldBg = Color(0xFF33290A),
    muted = Color(0xFF94A39A),
    cardBorder = Color(0xFF1F3329),
    skeleton = Color(0xFF1A2B22),
    isDark = true
)

private val LightScheme = lightColorScheme(
    primary = RuppPalette.Forest800,
    onPrimary = Color.White,
    primaryContainer = RuppPalette.Forest100,
    onPrimaryContainer = RuppPalette.Forest900,
    secondary = RuppPalette.Forest600,
    onSecondary = Color.White,
    secondaryContainer = RuppPalette.Green100,
    onSecondaryContainer = RuppPalette.Forest800,
    tertiary = RuppPalette.Gold,
    onTertiary = RuppPalette.Forest900,
    background = Color(0xFFF5F6F1),
    onBackground = Color(0xFF15201A),
    surface = Color.White,
    onSurface = Color(0xFF15201A),
    surfaceVariant = Color(0xFFF0F2EC),
    onSurfaceVariant = Color(0xFF5F6B63),
    outline = Color(0xFFD6DCD3),
    outlineVariant = Color(0xFFE4E8E2),
    error = Color(0xFFDC2626),
    // Sheets, dialogs and menus use these; without them Material falls back to a lavender tint.
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8F9F5),
    surfaceContainer = Color(0xFFF5F6F1),
    surfaceContainerHigh = Color(0xFFF0F2EC),
    surfaceContainerHighest = Color(0xFFEAEDE6),
    surfaceBright = Color.White,
    surfaceDim = Color(0xFFDDE2DA),
    surfaceTint = RuppPalette.Forest700
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF8FD3AC),
    onPrimary = RuppPalette.Forest900,
    primaryContainer = Color(0xFF17402E),
    onPrimaryContainer = Color(0xFFD5F0DF),
    secondary = RuppPalette.Green400,
    onSecondary = Color(0xFF003919),
    secondaryContainer = Color(0xFF14382A),
    onSecondaryContainer = RuppPalette.Green100,
    tertiary = RuppPalette.Gold,
    onTertiary = RuppPalette.Forest900,
    background = Color(0xFF07110C),
    onBackground = Color(0xFFE6EEE8),
    surface = Color(0xFF0E1C15),
    onSurface = Color(0xFFE6EEE8),
    surfaceVariant = Color(0xFF14261D),
    onSurfaceVariant = Color(0xFF94A39A),
    outline = Color(0xFF29402F),
    outlineVariant = Color(0xFF1F3329),
    error = Color(0xFFF87171),
    surfaceContainerLowest = Color(0xFF07110C),
    surfaceContainerLow = Color(0xFF0E1C15),
    surfaceContainer = Color(0xFF12221A),
    surfaceContainerHigh = Color(0xFF17291F),
    surfaceContainerHighest = Color(0xFF1C3025),
    surfaceBright = Color(0xFF213A2D),
    surfaceDim = Color(0xFF07110C),
    surfaceTint = Color(0xFF8FD3AC)
)

private val RuppTypography = Typography(
    displaySmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.3).sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 25.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 0.6.sp)
)

val LocalRuppColors = staticCompositionLocalOf { LightRupp }

/** Shortcut: `Rupp.colors.success`. */
object Rupp {
    val colors: RuppColors
        @Composable get() = LocalRuppColors.current
}

@Composable
fun RuppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as? Activity)?.window?.let { window ->
                WindowCompat.getInsetsController(window, view).apply {
                    // Headers are dark gradients, so status bar icons stay light on every screen.
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }
    CompositionLocalProvider(LocalRuppColors provides if (darkTheme) DarkRupp else LightRupp) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            typography = RuppTypography,
            content = content
        )
    }
}
