package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// -——— Light -———
val LightBackground    = Color(0xFFF0F4F9)
val LightSurface       = Color(0xFFFAFCFA) // section container
val LightTile          = Color(0xFFF7F9F7) // inner tiles
val LightIconCircle    = Color(0xFFDCE8DC)
val LightNavBar        = Color(0xFFFFFFFF)
val LightNavSelected   = Color(0xFFE3EDE3)
val LightNavUnselected = Color(0xFF6B7280)
val LightTextPrimary   = Color(0xFF1B1B1B)
val LightTextSecondary = Color(0xFF64748B)

// -——— Dark -———
val DarkBackground     = Color(0xFF2B2B2B)
val DarkSurface        = Color(0xFF1E1E1E)
val DarkTile           = Color(0xFF222222)
val DarkBorder         = Color(0xFF3A3A3A)
val DarkIconCircle     = Color(0xFF2A4A2C)
val DarkNavBar         = Color(0xFF1F1F1F)
val DarkNavSelected    = Color(0xFF2A4A2C)
val DarkTextPrimary    = Color(0xFFFFFFFF)
val DarkTextSecondary  = Color(0xFF9E9E9E)

// -——— Shared / brand -———
val GreenPrimary       = Color(0xFF2E7D32) // icons, selected nav (light)
val GreenSection       = Color(0xFF388E3C) // section titles (light)
val GreenAccentDark    = Color(0xFF4CAF50) // icons + titles (dark)
val ActiveMint         = Color(0xFF69F0AE) // “Active” badge text/dot/border
val CardNavy           = Color(0xFF0F2A2E) // card gradient end
val CardRowOverlay     = Color(0x661B3B2A) // “tap to view” bar
val EmblemGold         = Color(0x558A7A2E) // watermark
val ProfileRing        = Color(0xFFFFFFFF)

// -——— Header -———
val HeaderTop          = Color(0xFF1F2F2A)
val HeaderBottom       = Color(0xFF2E7D32)
val HeaderOverlay      = Brush.verticalGradient(listOf(HeaderTop.copy(alpha = .85f), HeaderBottom))

// -——— ID card gradients -———
val CardGradientLight  = Brush.linearGradient(listOf(Color(0xFF2E7D32), CardNavy))
val CardGradientDark   = Brush.linearGradient(listOf(Color(0xFF4CAF50), CardNavy))
