package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RuppDarkColorScheme = darkColorScheme(
    primary = RuppPrimaryGreen,
    onPrimary = RuppTextPrimary,
    primaryContainer = RuppCardGradientStart,
    onPrimaryContainer = RuppLightGreen,
    secondary = RuppAccentGreen,
    onSecondary = RuppTextPrimary,
    background = RuppDarkBg,
    onBackground = RuppTextPrimary,
    surface = RuppCardBg,
    onSurface = RuppTextPrimary,
    surfaceVariant = RuppCardElevated,
    onSurfaceVariant = RuppTextSecondary,
    outline = RuppCardBorder
)

private val RuppLightColorScheme = RuppDarkColorScheme // Default to dark aesthetic as shown in screenshot

@Composable
fun RUPP_Payment_Systerm_Moblie_InterfaceTheme(
    darkTheme: Boolean = true, // Default to app dark green design
    dynamicColor: Boolean = false, // Preserve RUPP brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = RuppDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
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