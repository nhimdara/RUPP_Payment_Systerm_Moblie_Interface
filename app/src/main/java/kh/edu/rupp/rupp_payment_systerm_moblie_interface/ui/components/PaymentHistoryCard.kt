package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistoryItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAbaBlue
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAccentGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardElevated
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppKhqrRed
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusFailed
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusFailedBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusPending
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusPendingBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusSuccess
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusSuccessBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary


@Composable
fun PaymentHistoryCard(
    item: PaymentHistoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (item.category) {
        PaymentCategory.TUITION -> Icons.Default.School
        PaymentCategory.EXAM -> Icons.AutoMirrored.Filled.Assignment
        PaymentCategory.SERVICES -> Icons.Default.Badge
        PaymentCategory.CERTIFICATE -> Icons.Default.Description
        PaymentCategory.TOP_UP -> Icons.Default.AccountBalanceWallet
        PaymentCategory.ALL -> Icons.Default.Description
    }

    val iconBgColor: Color = when (item.category) {
        PaymentCategory.TOP_UP -> RuppPrimaryGreen.copy(alpha = 0.15f)
        PaymentCategory.TUITION -> Color(0xFF19382B)
        PaymentCategory.EXAM -> Color(0xFF1B2E3D)
        PaymentCategory.SERVICES -> Color(0xFF2A2E20)
        PaymentCategory.CERTIFICATE -> Color(0xFF2C2237)
        PaymentCategory.ALL -> RuppCardElevated
    }

    val iconTintColor: Color = when (item.category) {
        PaymentCategory.TOP_UP -> RuppLightGreen
        PaymentCategory.TUITION -> RuppAccentGreen
        PaymentCategory.EXAM -> Color(0xFF60A5FA)
        PaymentCategory.SERVICES -> Color(0xFFFBBF24)
        PaymentCategory.CERTIFICATE -> Color(0xFFC084FC)
        PaymentCategory.ALL -> RuppLightGreen
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(RuppCardBg)
            .border(1.dp, RuppCardBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Icon in Squircle
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = item.category.displayName,
                tint = iconTintColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Middle: Title, Date & Method
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuppTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${item.date} • ${item.time}",
                fontSize = 12.sp,
                color = RuppTextSecondary
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Method tag & Category Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Method indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(RuppCardElevated)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.paymentMethod.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (item.paymentMethod) {
                            PaymentMethod.KHQR -> RuppKhqrRed
                            PaymentMethod.ABA_PAY -> RuppAbaBlue
                            PaymentMethod.STUDENT_BALANCE -> RuppLightGreen
                            else -> RuppTextSecondary
                        }
                    )
                }

                // Reference snippet
                Text(
                    text = item.transactionRef,
                    fontSize = 10.sp,
                    color = RuppTextMuted,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right: Amount and Status Pill
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val amountPrefix = if (item.isDeduction) "-$" else "+$"
            val amountColor = if (item.isDeduction) RuppTextPrimary else RuppLightGreen

            Text(
                text = "$amountPrefix${String.format("%.2f", item.amount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )

            // Status Badge Pill
            StatusBadge(status = item.status)
        }
    }
}

@Composable
fun StatusBadge(status: PaymentStatus) {
    val (bgColor, textColor, icon) = when (status) {
        PaymentStatus.COMPLETED -> Triple(
            RuppStatusSuccessBg,
            RuppStatusSuccess,
            Icons.Default.Check
        )
        PaymentStatus.PENDING -> Triple(
            RuppStatusPendingBg,
            RuppStatusPending,
            Icons.Default.Schedule
        )
        PaymentStatus.FAILED -> Triple(
            RuppStatusFailedBg,
            RuppStatusFailed,
            Icons.Default.Close
        )
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status.displayName,
            tint = textColor,
            modifier = Modifier.size(10.dp)
        )
        Text(
            text = status.displayName,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}