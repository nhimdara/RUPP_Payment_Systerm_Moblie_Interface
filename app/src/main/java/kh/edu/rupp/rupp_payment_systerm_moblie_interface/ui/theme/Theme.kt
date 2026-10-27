package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenAccentDark,
    onPrimary = Color.Black,
    primaryContainer = DarkIconCircle,
    onPrimaryContainer = Color.White,
    secondary = GreenSection,
    onSecondary = Color.White,
    tertiary = ActiveMint,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkTile,
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = LightNavSelected,
    onPrimaryContainer = GreenPrimary,
    secondary = GreenSection,
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

@Composable
fun RUPP_Payment_Systerm_Moblie_InterfaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ (defaults to false to preserve brand green colors)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
