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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FeeStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem

@Composable
fun SchoolFeeCard(
    item: SchoolFeeItem,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (SchoolFeeItem) -> Unit = {}
) {
    val cardBg = if (isDarkMode) Color(0xFF161F1B) else Color.White
    val cardBorder = if (isDarkMode) Color(0xFF24322B) else Color(0xFFE5E7EB)
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val textSecondary = if (isDarkMode) Color(0xFF90A198) else Color(0xFF6B7280)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(width = 1.dp, color = cardBorder, shape = RoundedCornerShape(18.dp))
            .clickable { onClick(item) }
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.subtitle,
                    color = textSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("$%.2f", item.totalAmount),
                    color = textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                val (badgeBg, badgeText, badgeLabel) = when (item.status) {
                    FeeStatus.UNPAID -> Triple(
                        if (isDarkMode) Color(0xFF4D391A) else Color(0xFFFEF3C7),
                        if (isDarkMode) Color(0xFFFDE68A) else Color(0xFFB45309),
                        "Unpaid"
                    )
                    FeeStatus.FAILED -> Triple(
                        if (isDarkMode) Color(0xFF5C2320) else Color(0xFFFEE2E2),
                        if (isDarkMode) Color(0xFFFFB4AB) else Color(0xFFDC2626),
                        "Retry"
                    )
                    FeeStatus.PAID -> Triple(
                        if (isDarkMode) Color(0xFF163A29) else Color(0xFFE6F4EA),
                        if (isDarkMode) Color(0xFF6EE7B7) else Color(0xFF137333),
                        "Paid"
                    )
                    FeeStatus.PENDING -> Triple(
                        if (isDarkMode) Color(0xFF253342) else Color(0xFFEFF6FF),
                        if (isDarkMode) Color(0xFF93C5FD) else Color(0xFF2563EB),
                        "Pending"
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        color = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
