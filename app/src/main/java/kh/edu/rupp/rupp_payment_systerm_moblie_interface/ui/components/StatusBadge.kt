package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenAccentDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary

// Status colours suggested by the API doc: PENDING amber, VERIFIED blue, CONFIRMED green,
// REJECTED / CANCELLED red, REFUNDED grey. Each has a lighter variant for dark backgrounds.
private class StatusColor(val onLight: Color, val onDark: Color)

private val Amber = StatusColor(Color(0xFFB45309), Color(0xFFFBBF24))
private val Blue = StatusColor(Color(0xFF1D4ED8), Color(0xFF60A5FA))
private val Green = StatusColor(GreenPrimary, GreenAccentDark)
private val Red = StatusColor(Color(0xFFC62828), Color(0xFFEF5350))
private val Grey = StatusColor(Color(0xFF6B7280), Color(0xFF9CA3AF))

// True when the current theme background is dark (works with any colour scheme)
@Composable
fun isDarkBackground(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

@Composable
fun StatusBadge(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.5f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
fun PaymentStatusBadge(
    status: PaymentStatus,
    modifier: Modifier = Modifier,
    onDarkBackground: Boolean = isDarkBackground()
) {
    val (label, color) = when (status) {
        PaymentStatus.PENDING -> "Pending" to Amber
        PaymentStatus.VERIFIED -> "Verified" to Blue
        PaymentStatus.CONFIRMED -> "Confirmed" to Green
        PaymentStatus.REJECTED -> "Rejected" to Red
        PaymentStatus.CANCELLED -> "Cancelled" to Red
        PaymentStatus.REFUNDED -> "Refunded" to Grey
    }
    StatusBadge(label, if (onDarkBackground) color.onDark else color.onLight, modifier)
}

// For TuitionCalculationResponse.paymentStatus, a display string: PAID, PARTIAL or UNPAID
@Composable
fun TuitionStatusBadge(
    status: String,
    modifier: Modifier = Modifier,
    onDarkBackground: Boolean = isDarkBackground()
) {
    val (label, color) = when (status.uppercase()) {
        "PAID" -> "Paid" to Green
        "PARTIAL" -> "Partially paid" to Amber
        "UNPAID" -> "Unpaid" to Red
        else -> status to Grey
    }
    StatusBadge(label, if (onDarkBackground) color.onDark else color.onLight, modifier)
}

@Composable
fun ActiveBadge(
    modifier: Modifier = Modifier,
    onDarkBackground: Boolean = isDarkBackground()
) {
    StatusBadge("Active", if (onDarkBackground) ActiveMint else GreenPrimary, modifier)
}
