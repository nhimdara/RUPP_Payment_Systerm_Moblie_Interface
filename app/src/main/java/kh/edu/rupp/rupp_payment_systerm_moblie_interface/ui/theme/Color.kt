package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// -——— RUPP Dark Theme Palette (matching screenshot) -———
val RuppDarkBg = Color(0xFF0E1411)
val RuppCardBg = Color(0xFF161F1B)
val RuppCardElevated = Color(0xFF1B2621)
val RuppCardBorder = Color(0xFF24322B)
val RuppCardBorderSubtle = Color(0x1F34D399)

// -——— Brand Greens -———
val RuppPrimaryGreen = Color(0xFF00A86B)
val RuppAccentGreen = Color(0xFF10B981)
val RuppLightGreen = Color(0xFF34D399)
val RuppSoftGreen = Color(0xFF059669)

// -——— Gradients -———
val RuppCardGradientStart = Color(0xFF1D4233)
val RuppCardGradientEnd = Color(0xFF0E2118)
val RuppTopUpGradientStart = Color(0xFF00A86B)
val RuppTopUpGradientEnd = Color(0xFF059669)

// -——— Typography & Content Colors -———
val RuppTextPrimary = Color(0xFFFFFFFF)
val RuppTextSecondary = Color(0xFF90A198)
val RuppTextMuted = Color(0xFF62736B)

// -——— Transaction Status Colors -———
val RuppStatusSuccess = Color(0xFF10B981)
val RuppStatusSuccessBg = Color(0xFF12281F)
val RuppStatusPending = Color(0xFFF59E0B)
val RuppStatusPendingBg = Color(0xFF29200D)
val RuppStatusFailed = Color(0xFFEF4444)
val RuppStatusFailedBg = Color(0xFF2B1313)

// -——— Payment Methods & Avatar Accents -———
val RuppKhqrRed = Color(0xFFDE3618)
val RuppAbaBlue = Color(0xFF096894)
val RuppAvatarBg = Color(0xFF362F24)
val RuppAvatarText = Color(0xFFE7D8BD)
val RuppBarcodeGreen = Color(0xFF4C7B67)

// -——— Dev / Shared Palette -———
// Light
val LightBackground    = Color(0xFFF0F4F9)
val LightSurface       = Color(0xFFFAFCFA) // section container
val LightTile          = Color(0xFFF7F9F7) // inner tiles
val LightIconCircle    = Color(0xFFDCE8DC)
val LightNavBar        = Color(0xFFFFFFFF)
val LightNavSelected   = Color(0xFFE3EDE3)
val LightNavUnselected = Color(0xFF6B7280)
val LightTextPrimary   = Color(0xFF1B1B1B)

// Dark
val DarkBackground     = Color(0xFF2B2B2B)
val DarkSurface        = Color(0xFF1E1E1E)
val DarkTile           = Color(0xFF222222)
val DarkBorder         = Color(0xFF3A3A3A)
val DarkIconCircle     = Color(0xFF2A4A2C)
val DarkNavBar         = Color(0xFF1F1F1F)
val DarkNavSelected    = Color(0xFF2A4A2C)
val DarkTextPrimary    = Color(0xFFFFFFFF)

// Shared / brand
val GreenPrimary       = Color(0xFF2E7D32) // icons, selected nav (light)
val GreenSection       = Color(0xFF388E3C) // section titles (light)
val GreenAccentDark    = Color(0xFF4CAF50) // icons + titles (dark)
val ActiveMint         = Color(0xFF69F0AE) // “Active” badge text/dot/border
val CardNavy           = Color(0xFF0F2A2E) // card gradient end
val CardRowOverlay     = Color(0x661B3B2A) // “tap to view” bar
val EmblemGold         = Color(0x558A7A2E) // watermark
val ProfileRing        = Color(0xFFFFFFFF)

// Header
val HeaderTop          = Color(0xFF1F2F2A)
val HeaderBottom       = Color(0xFF2E7D32)
val HeaderOverlay      = Brush.verticalGradient(listOf(HeaderTop.copy(alpha = .85f), HeaderBottom))

// ID card gradients
val CardGradientLight  = Brush.linearGradient(listOf(Color(0xFF2E7D32), CardNavy))
val CardGradientDark   = Brush.linearGradient(listOf(Color(0xFF4CAF50), CardNavy))

// Fallback Material3 definitions
val Purple80 = RuppLightGreen
val PurpleGrey80 = RuppTextSecondary
val Pink80 = RuppPrimaryGreen
val Purple40 = RuppPrimaryGreen
val PurpleGrey40 = RuppCardBg
val Pink40 = RuppDarkBg
