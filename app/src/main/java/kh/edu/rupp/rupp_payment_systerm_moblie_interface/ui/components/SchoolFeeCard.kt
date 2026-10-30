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
import androidx.compose.foundation.shape.CircleShape
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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgePaidBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgePaidText
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgeRetryBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgeRetryText
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgeUnpaidBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.BadgeUnpaidText
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextLightGray
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextWhite

@Composable
fun SchoolFeeCard(
    item: SchoolFeeItem,
    modifier: Modifier = Modifier,
    onClick: (SchoolFeeItem) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(DarkSurface)
            .border(width = 1.dp, color = DarkBorder, shape = RoundedCornerShape(22.dp))
            .clickable { onClick(item) }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Title & Subtitle (Icon removed for cleaner layout)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    color = TextLightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right: Amount + Status Badge
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = String.format("$%.2f", item.totalAmount),
                    color = TextWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                val (badgeBg, badgeText) = when (item.status) {
                    FeeStatus.UNPAID -> Pair(BadgeUnpaidBg, BadgeUnpaidText)
                    FeeStatus.FAILED -> Pair(BadgeRetryBg, BadgeRetryText)
                    FeeStatus.PAID -> Pair(BadgePaidBg, BadgePaidText)
                    FeeStatus.PENDING -> Pair(Color(0xFF253342), Color(0xFF93C5FD))
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(badgeBg)
                        .padding(horizontal = 11.dp, vertical = 3.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.status.label,
                        color = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
