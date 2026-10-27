package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Animated Loading / Splash Screen for RUPP Payment System
 * Features official university branding, dynamic progress tracking,
 * and academic security indicators.
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    durationMs: Long = 2400L,
    onLoadingComplete: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    // Luxury forest green gradient background for splash
    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF06140C),
                Color(0xFF0C2918),
                Color(0xFF07180E)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F3520),
                Color(0xFF1E5638),
                Color(0xFF113D25)
            )
        )
    }

    // Breathing pulse animation for RUPP Emblem
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emblem_scale"
    )

    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_glow"
    )

    // Progress State (0f to 1f)
    var rawProgress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = rawProgress,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        ),
        label = "progress_bar_anim"
    )

    // Dynamic status text based on progress
    val statusMessage = when {
        animatedProgress < 0.25f -> "Initializing secure environment..."
        animatedProgress < 0.55f -> "Verifying security certificates..."
        animatedProgress < 0.82f -> "Connecting to RUPP Payment Gateway..."
        animatedProgress < 0.98f -> "Loading student & faculty services..."
        else -> "System ready! Launching..."
    }

    // Simulated loading sequence
    LaunchedEffect(Unit) {
        val stepDelay = durationMs / 5
        rawProgress = 0.18f
        delay(stepDelay)
        rawProgress = 0.45f
        delay(stepDelay)
        rawProgress = 0.75f
        delay(stepDelay)
        rawProgress = 0.95f
        delay(stepDelay)
        rawProgress = 1.0f
        delay(400) // Brief pause at 100%
        onLoadingComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Decorative background geometric circles
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-80).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.03f))
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.03f))
        )

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Emblem with glowing halo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Outer glowing ambient ring
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(scaleAnim)
                        .clip(CircleShape)
                        .background(ActiveMint.copy(alpha = haloAlpha * 0.3f))
                )

                // Secondary soft gold ring
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(scaleAnim * 0.98f)
                        .clip(CircleShape)
                        .background(Color(0xFFE5B842).copy(alpha = 0.25f))
                )

                // Solid white circular badge holding RUPP seal
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFFD4AF37)),
                    shadowElevation = 16.dp,
                    modifier = Modifier
                        .size(112.dp)
                        .scale(scaleAnim)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rupp_logo),
                        contentDescription = "RUPP Seal",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary App Title
            Text(
                text = "RUPP PAYMENT SYSTEM",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Official Khmer university name
            Text(
                text = "សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ",
                color = ActiveMint,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Royal University of Phnom Penh",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Progress Bar Container
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.82f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Linear Progress Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50),
                                        ActiveMint,
                                        Color(0xFFFFD54F)
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Percentage & status row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusMessage,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        color = ActiveMint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom Trust & Version Badges
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Security badge pill
            Surface(
                color = Color.Black.copy(alpha = 0.28f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = "SSL Encrypted",
                        tint = ActiveMint,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "256-bit SSL End-to-End Encryption",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Royal University of Phnom Penh • ICT Center",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 11.sp
            )

            Text(
                text = "v1.0.0 (Build 2026)",
                color = Color.White.copy(alpha = 0.35f),
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Reusable full-screen or dialog Loading Overlay for network actions (Login, Register, Payment)
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    message: String = "Processing request...",
    onDismissRequest: () -> Unit = {}
) {
    if (!isLoading) return

    val isDark = isSystemInDarkTheme()
    val dialogBg = if (isDark) DarkSurface else LightSurface
    val textPrimary = if (isDark) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDark) DarkTextSecondary else LightTextSecondary

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = dialogBg,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDark) DarkBorder else Color(0xFFE2E8F0)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(68.dp)
                ) {
                    CircularProgressIndicator(
                        color = GreenPrimary,
                        strokeWidth = 3.5.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(46.dp),
                        shadowElevation = 2.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rupp_logo),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = message,
                    color = textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Please wait a moment",
                    color = textSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -——— Compose Previews -———

@Preview(name = "Loading Screen - Default", showBackground = true)
@Composable
fun LoadingScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        LoadingScreen()
    }
}

@Preview(name = "Loading Screen - Dark Mode", showBackground = true)
@Composable
fun LoadingScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        LoadingScreen()
    }
}

@Preview(name = "Loading Overlay - Light", showBackground = true)
@Composable
fun LoadingOverlayPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        LoadingOverlay(isLoading = true, message = "Authenticating student credentials...")
    }
}
