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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistorySummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAccentGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorderSubtle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardGradientEnd
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardGradientStart
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0E1411,
    name = "Payment Summary Card",
    showSystemUi = false
)
@Composable
fun PaymentHistorySummaryCardPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(
        darkTheme = true
    ) {
        PaymentHistorySummaryCard(
            summary = PaymentHistorySummary(
                academicYear = "2026-2027",
                totalPaid = 960.00,
                currentBalance = 120.00,
                totalTopUp = 120.00,
                transactionCount = 9
            ),
            onDownloadStatementClick = {}
        )
    }
}
@Composable
fun PaymentHistorySummaryCard(
    summary: PaymentHistorySummary,
    modifier: Modifier = Modifier,
    onDownloadStatementClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        RuppCardGradientStart,
                        Color(0xFF142B21),
                        RuppCardGradientEnd
                    )
                )
            )
            .border(
                width = 1.dp,
                color = RuppCardBorderSubtle,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            // Header Row: University badge & Academic Year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(RuppPrimaryGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "RUPP Seal",
                            tint = RuppLightGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ROYAL UNIVERSITY OF PHNOM PENH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RuppLightGreen,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "RUPP Tuition & Campus Services",
                            fontSize = 11.sp,
                            color = RuppTextSecondary
                        )
                    }
                }

                // Academic Year Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF203B2F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = summary.academicYear,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuppLightGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Total Paid Amount Section
            Text(
                text = "Total Paid (Academic Year)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuppTextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", summary.totalPaid)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RuppTextPrimary
                )

                // Download Statement Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RuppPrimaryGreen.copy(alpha = 0.18f))
                        .border(1.dp, RuppPrimaryGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .clickable { onDownloadStatementClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Statement",
                        tint = RuppLightGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Statement",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuppLightGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sub Stats Divider Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF1F382B))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Three stats: Student Balance, Top-ups, Completed count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Student Balance",
                        fontSize = 11.sp,
                        color = RuppTextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$${String.format("%.2f", summary.currentBalance)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppAccentGreen
                    )
                }

                Column {
                    Text(
                        text = "Total Top-up",
                        fontSize = 11.sp,
                        color = RuppTextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "+$${String.format("%.2f", summary.totalTopUp)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppLightGreen
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Transactions",
                        fontSize = 11.sp,
                        color = RuppTextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${summary.transactionCount} records",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppTextPrimary
                    )
                }
            }
        }
    }
}
