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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionDashboardSummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionPaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.CardNavy
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenAccentDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary
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

/**
 * Modern Student Profile Summary Card.
 * Displays official student credentials, RUPP enrollment status, and dynamic tuition billing overview.
 */
@Composable
fun StudentProfileSummaryCard(
    student: StudentProfile,
    summary: TuitionDashboardSummary,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onViewBreakdownClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        RuppCardGradientStart,
                        Color(0xFF142B21),
                        CardNavy,
                        RuppCardGradientEnd
                    )
                )
            )
            .border(
                width = 1.dp,
                color = RuppCardBorderSubtle,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onCardClick() }
            .padding(20.dp)
    ) {
        Column {
            // Header Row: University branding & Active status badge
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(RuppPrimaryGreen.copy(alpha = 0.22f))
                            .border(1.dp, RuppLightGreen.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "RUPP Emblem",
                            tint = RuppLightGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ROYAL UNIVERSITY OF PHNOM PENH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RuppLightGreen,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Student Electronic Billing ID",
                            fontSize = 11.sp,
                            color = RuppTextSecondary
                        )
                    }
                }

                // Active Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ActiveMint.copy(alpha = 0.15f))
                        .border(1.dp, ActiveMint.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(ActiveMint)
                    )
                    Text(
                        text = "Active",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ActiveMint
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Student Identity Info: Avatar initials + Full Name + Khmer Name + ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Avatar Initials
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(
                                    RuppPrimaryGreen,
                                    ActiveMint,
                                    Color(0xFF2E7D32),
                                    RuppPrimaryGreen
                                )
                            )
                        )
                        .padding(2.5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF132A20)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.initials,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppLightGreen
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.fullName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppTextPrimary
                    )

                    if (student.khmerName.isNotBlank()) {
                        Text(
                            text = student.khmerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = RuppTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1F3D2F))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = student.studentId.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RuppLightGreen
                            )
                        }

                        Text(
                            text = "${student.faculty} • Year ${student.year}",
                            fontSize = 11.sp,
                            color = RuppTextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtle divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0x3334D399))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tuition Snapshot Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Current Term Balance (${summary.semesterName})",
                        fontSize = 11.sp,
                        color = RuppTextSecondary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$${String.format("%.2f", summary.remainingBalance)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (summary.remainingBalance == 0.0) ActiveMint else Color(0xFFFBBF24)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Status tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (summary.paymentStatus) {
                                        TuitionPaymentStatus.PAID -> Color(0xFF122C1D)
                                        TuitionPaymentStatus.PARTIAL -> Color(0xFF33260E)
                                        TuitionPaymentStatus.UNPAID -> Color(0xFF3B1515)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = summary.paymentStatus.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (summary.paymentStatus) {
                                    TuitionPaymentStatus.PAID -> RuppAccentGreen
                                    TuitionPaymentStatus.PARTIAL -> Color(0xFFFBBF24)
                                    TuitionPaymentStatus.UNPAID -> Color(0xFFF87171)
                                }
                            )
                        }
                    }
                }

                // CTA Button: View Details
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RuppPrimaryGreen.copy(alpha = 0.22f))
                        .border(1.dp, RuppPrimaryGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { onViewBreakdownClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Fee Breakdown",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuppLightGreen
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Navigate to Fees",
                        tint = RuppLightGreen,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun StudentProfileSummaryCardPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        StudentProfileSummaryCard(
            student = SampleStudentProfile.leangkhai,
            summary = SampleDashboardData.tuitionSummary,
            modifier = Modifier.padding(16.dp)
        )
    }
}
