package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PayMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusChip
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.paymentStatusStyle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.serviceVisual

fun methodIcon(code: String?): ImageVector {
    val c = code?.uppercase().orEmpty()
    return when {
        c.contains("KHQR") || c.contains("BAKONG") -> Icons.Rounded.QrCode2
        c.startsWith("ABA") || c.startsWith("ACLEDA") -> Icons.Rounded.AccountBalance
        c.startsWith("WING") -> Icons.Rounded.Wallet
        else -> Icons.Rounded.Payments
    }
}

/** Title for a payment row. The backend has no fee name, so build one from the academic year. */
fun PaymentResponse.title(): String =
    if (isService) serviceFeeName ?: "Campus service"
    else "Tuition Fee" + (academicYearLabel?.let { " $it" } ?: "")

@Composable
fun PaymentListItem(payment: PaymentResponse, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val method = payment.details.firstOrNull()
    val style = paymentStatusStyle(payment.status)
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(
            if (payment.isService) serviceVisual(payment.serviceFeeCategory).icon else methodIcon(method?.method),
            style.fg,
            style.bg
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(payment.title(), style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                "${PayMethod.label(method?.method)} · ${Fmt.dateLong(payment.paymentDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = Rupp.colors.muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            payment.receipt?.receiptNumber?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = Rupp.colors.success)
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(Fmt.usd(payment.totalPaid), style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            StatusChip(style)
        }
    }
}
