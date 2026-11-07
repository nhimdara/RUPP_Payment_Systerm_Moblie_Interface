package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// RUPP Brand Dark Color Scheme
val RuppDarkColorScheme = darkColorScheme(
    primary = RuppPrimaryGreen,
    onPrimary = RuppTextPrimary,
    primaryContainer = RuppCardGradientStart,
    onPrimaryContainer = RuppLightGreen,
    secondary = RuppAccentGreen,
    onSecondary = RuppTextPrimary,
    tertiary = RuppLightGreen,
    onTertiary = RuppDarkBg,
    background = RuppDarkBg,
    onBackground = RuppTextPrimary,
    surface = RuppCardBg,
    onSurface = RuppTextPrimary,
    surfaceVariant = RuppCardElevated,
    onSurfaceVariant = RuppTextSecondary,
    outline = RuppCardBorder
)

// Light Color Scheme
val RuppLightColorScheme = lightColorScheme(
    primary = RuppPrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = LightNavSelected,
    onPrimaryContainer = RuppPrimaryGreen,
    secondary = RuppAccentGreen,
    onSecondary = Color.White,
    tertiary = ActiveMint,
    onTertiary = Color(0xFF00390E),
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightTile,
    onSurfaceVariant = LightNavUnselected,
    outline = Color(0xFFE0E0E0)
)

// Aliases for compatibility with both dev and feature branches
val DarkColorScheme = RuppDarkColorScheme
val LightColorScheme = RuppLightColorScheme

@Composable
fun RUPP_Payment_Systerm_Moblie_InterfaceTheme(
    darkTheme: Boolean = true, // Default to app dark green design
    // Dynamic color is available on Android 12+ (defaults to false to preserve brand green colors)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> RuppDarkColorScheme
        else -> RuppLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
