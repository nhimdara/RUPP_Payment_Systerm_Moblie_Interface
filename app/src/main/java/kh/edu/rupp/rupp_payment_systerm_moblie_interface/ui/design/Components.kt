package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus

// ---------------------------------------------------------------------------
// Surfaces
// ---------------------------------------------------------------------------

@Composable
fun RuppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, Rupp.colors.cardBorder, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding),
        content = content
    )
}

/** Navy-to-green gradient header used at the top of every main screen. */
@Composable
fun GradientHeader(
    modifier: Modifier = Modifier,
    bottomRadius: Dp = 28.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = bottomRadius, bottomEnd = bottomRadius))
            .background(Rupp.colors.hero)
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 20.dp),
        content = content
    )
}

/** Simple top bar for secondary screens, drawn on the gradient. */
@Composable
fun HeaderBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.14f))
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            if (subtitle != null) {
                Text(subtitle, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
            }
            Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        actions()
    }
}

/**
 * Solid bar behind the status bar that fades in once a full-page scroll moves the gradient
 * header away, so content never runs under the clock and icons.
 */
@Composable
fun BoxScope.StatusBarScrim(visible: Boolean) {
    val alpha by animateFloatAsState(if (visible) 1f else 0f, label = "scrim")
    Box(
        Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .alpha(alpha)
            .background(Rupp.colors.heroSolid)
    )
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(width = 4.dp, height = 16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Brush.verticalGradient(listOf(RuppPalette.Forest500, RuppPalette.Green500)))
        )
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        if (action != null && onAction != null) {
            Text(
                action,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = MaterialTheme.colorScheme.onSurface, bold: Boolean = false) {
    Row(modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.Top) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Rupp.colors.muted, modifier = Modifier.weight(0.42f))
        Text(
            value,
            style = if (bold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.58f)
        )
    }
}

@Composable
fun IconBadge(icon: ImageVector, tint: Color, background: Color, size: Dp = 42.dp, iconSize: Dp = 22.dp) {
    Box(
        Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(background),
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(iconSize)) }
}

@Composable
fun AvatarInitials(initials: String, size: Dp = 44.dp, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(RuppPalette.Gold, Color(0xFFF59E0B))))
            .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials.ifBlank { "?" },
            color = RuppPalette.Forest900,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.36f).sp
        )
    }
}

// ---------------------------------------------------------------------------
// Buttons
// ---------------------------------------------------------------------------

/** Navy-to-green gradient call to action, with a built-in loading state. */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    val active = enabled && !loading
    // Fade the whole button (background included) as one layer when disabled.
    Box(
        modifier
            .fillMaxWidth()
            .height(54.dp)
            .graphicsLayer { alpha = if (enabled) 1f else 0.5f }
            .clip(RoundedCornerShape(16.dp))
            .background(Rupp.colors.cta, RoundedCornerShape(16.dp))
            .clickable(enabled = active, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(22.dp))
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text(text, color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun SoftButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

// ---------------------------------------------------------------------------
// Status
// ---------------------------------------------------------------------------

data class StatusStyle(val label: String, val fg: Color, val bg: Color)

/** Colors from the API guide: PENDING amber, VERIFIED blue, CONFIRMED green, REJECTED/CANCELLED red, REFUNDED grey. */
@Composable
fun paymentStatusStyle(status: PaymentStatus?): StatusStyle {
    val c = Rupp.colors
    return when (status) {
        PaymentStatus.PENDING -> StatusStyle("Pending", c.warning, c.warningBg)
        PaymentStatus.VERIFIED -> StatusStyle("Verified", c.info, c.infoBg)
        PaymentStatus.CONFIRMED, PaymentStatus.COMPLETED -> StatusStyle("Confirmed", c.success, c.successBg)
        PaymentStatus.REJECTED, PaymentStatus.CANCELLED, PaymentStatus.FAILED ->
            StatusStyle(status.displayName, c.danger, c.dangerBg)
        PaymentStatus.REFUNDED -> StatusStyle("Refunded", c.neutral, c.neutralBg)
        null -> StatusStyle("Unknown", c.neutral, c.neutralBg)
    }
}

/** Tuition status from /tuition-fees/calculate: PAID, PARTIAL or UNPAID. */
@Composable
fun tuitionStatusStyle(status: String?): StatusStyle {
    val c = Rupp.colors
    return when (status?.uppercase()) {
        "PAID" -> StatusStyle("Paid", c.success, c.successBg)
        "PARTIAL" -> StatusStyle("Partially paid", c.warning, c.warningBg)
        else -> StatusStyle("Unpaid", c.danger, c.dangerBg)
    }
}

@Composable
fun StatusChip(style: StatusStyle, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(50))
            .background(style.bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(style.fg))
        Spacer(Modifier.width(6.dp))
        Text(style.label, color = style.fg, style = MaterialTheme.typography.labelMedium)
    }
}

/** Animated ring showing how much of the tuition has been paid. */
@Composable
fun ProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    stroke: Dp = 10.dp,
    trackColor: Color = Color.White.copy(alpha = 0.16f),
    brush: Brush = Brush.sweepGradient(listOf(RuppPalette.Green400, RuppPalette.Gold, RuppPalette.Green400)),
    center: @Composable () -> Unit = {}
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1100, easing = FastOutSlowInEasing),
        label = "ring"
    )
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val s = stroke.toPx()
            val inset = s / 2
            val arcSize = androidx.compose.ui.geometry.Size(this.size.width - s, this.size.height - s)
            drawArc(trackColor, 0f, 360f, false, topLeft = Offset(inset, inset), size = arcSize, style = Stroke(s))
            drawArc(brush, -90f, 360f * animated, false, topLeft = Offset(inset, inset), size = arcSize, style = Stroke(s, cap = StrokeCap.Round))
        }
        center()
    }
}

// ---------------------------------------------------------------------------
// Payment status timeline (my addition: shows where a payment is in the backend lifecycle)
// ---------------------------------------------------------------------------

data class TimelineStep(val title: String, val subtitle: String, val state: StepState)
enum class StepState { DONE, CURRENT, UPCOMING, FAILED }

fun lifecycleSteps(status: PaymentStatus?, submittedAt: String?, receiptNumber: String?): List<TimelineStep> {
    val submitted = TimelineStep("Payment submitted", Fmt.dateTime(submittedAt), StepState.DONE)
    return when (status) {
        PaymentStatus.PENDING -> listOf(
            submitted,
            TimelineStep("OTP verification", "Confirm with the 6-digit code", StepState.CURRENT),
            TimelineStep("Finance confirmation", "RUPP Finance Office reviews it", StepState.UPCOMING),
            TimelineStep("Official receipt", "Issued after confirmation", StepState.UPCOMING)
        )
        PaymentStatus.VERIFIED -> listOf(
            submitted,
            TimelineStep("OTP verified", "Your identity was confirmed", StepState.DONE),
            TimelineStep("Finance confirmation", "Waiting for RUPP Finance Office", StepState.CURRENT),
            TimelineStep("Official receipt", "Issued after confirmation", StepState.UPCOMING)
        )
        PaymentStatus.CONFIRMED, PaymentStatus.COMPLETED -> listOf(
            submitted,
            TimelineStep("OTP verified", "Identity confirmed", StepState.DONE),
            TimelineStep("Finance confirmed", "Approved by RUPP Finance Office", StepState.DONE),
            TimelineStep("Official receipt", receiptNumber ?: "Receipt issued", StepState.DONE)
        )
        PaymentStatus.REFUNDED -> listOf(
            submitted,
            TimelineStep("Finance confirmed", "Approved", StepState.DONE),
            TimelineStep("Refunded", "The amount was returned", StepState.DONE)
        )
        else -> listOf(
            submitted,
            TimelineStep(status?.displayName ?: "Stopped", "Contact the Finance Office if this is unexpected", StepState.FAILED)
        )
    }
}

@Composable
fun StatusTimeline(steps: List<TimelineStep>, modifier: Modifier = Modifier) {
    val c = Rupp.colors
    Column(modifier) {
        steps.forEachIndexed { index, step ->
            val color = when (step.state) {
                StepState.DONE -> c.success
                StepState.CURRENT -> c.warning
                StepState.UPCOMING -> c.neutral.copy(alpha = 0.5f)
                StepState.FAILED -> c.danger
            }
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(28.dp)) {
                    if (step.state == StepState.CURRENT) {
                        PulsingDot(color)
                    } else {
                        Box(
                            Modifier.size(22.dp).clip(CircleShape)
                                .background(if (step.state == StepState.UPCOMING) Color.Transparent else color.copy(alpha = 0.15f))
                                .border(2.dp, color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (step.state == StepState.DONE) {
                                Icon(Icons.Rounded.CheckCircle, null, tint = color, modifier = Modifier.size(22.dp))
                            }
                        }
                    }
                    if (index < steps.lastIndex) {
                        Box(
                            Modifier.width(2.dp).height(30.dp)
                                .background(if (step.state == StepState.DONE) c.success.copy(alpha = 0.5f) else c.cardBorder)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.padding(bottom = 10.dp)) {
                    Text(
                        step.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (step.state == StepState.UPCOMING) c.muted else MaterialTheme.colorScheme.onSurface
                    )
                    Text(step.subtitle, style = MaterialTheme.typography.bodySmall, color = c.muted)
                }
            }
        }
    }
}

@Composable
private fun PulsingDot(color: Color) {
    val t = rememberInfiniteTransition(label = "pulse")
    val scale by t.animateFloat(0.6f, 1f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "s")
    Box(Modifier.size(22.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.size(22.dp * scale).clip(CircleShape).background(color.copy(alpha = 0.25f)))
        Icon(Icons.Rounded.HourglassTop, null, tint = color, modifier = Modifier.size(14.dp))
    }
}

// ---------------------------------------------------------------------------
// Loading / empty / error
// ---------------------------------------------------------------------------

/** Shimmering placeholder block for skeleton loading. */
@Composable
fun Skeleton(modifier: Modifier = Modifier, radius: Dp = 12.dp) {
    val t = rememberInfiniteTransition(label = "shimmer")
    val x by t.animateFloat(-300f, 900f, infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "x")
    val base = Rupp.colors.skeleton
    Box(
        modifier
            .clip(RoundedCornerShape(radius))
            .background(
                Brush.linearGradient(
                    colors = listOf(base, base.copy(alpha = 0.45f), base),
                    start = Offset(x, 0f),
                    end = Offset(x + 300f, 300f)
                )
            )
    )
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconBadge(Icons.Rounded.CloudOff, Rupp.colors.danger, Rupp.colors.dangerBg, size = 64.dp, iconSize = 30.dp)
        Text("Couldn't load data", style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Rupp.colors.muted, textAlign = TextAlign.Center)
        SoftButton("Try again", onClick = onRetry, modifier = Modifier.width(180.dp))
    }
}

@Composable
fun EmptyState(title: String, message: String, modifier: Modifier = Modifier, icon: ImageVector = Icons.Rounded.Inbox) {
    Column(
        modifier.fillMaxWidth().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconBadge(icon, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, size = 64.dp, iconSize = 30.dp)
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = Rupp.colors.muted, textAlign = TextAlign.Center)
    }
}

@Composable
fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    Text(
        message,
        color = Rupp.colors.danger,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Rupp.colors.dangerBg)
            .padding(12.dp)
    )
}
