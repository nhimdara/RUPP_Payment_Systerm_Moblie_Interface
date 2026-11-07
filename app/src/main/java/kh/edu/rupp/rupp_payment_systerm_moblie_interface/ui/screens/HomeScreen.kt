package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.CampusAnnouncement
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.RecentPaymentActivity
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionDashboardSummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.StudentProfileSummaryCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenAccentDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAbaBlue
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAccentGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorderSubtle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardElevated
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppKhqrRed
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusSuccess
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary

/**
 * Student Home Dashboard.
 * Main hub for RUPP students featuring greeting, Student Profile Summary Card,
 * quick action hub, recent payment activity timeline, and campus announcements.
 */
@Composable
fun HomeScreen(
    student: StudentProfile = SampleStudentProfile.leangkhai,
    summary: TuitionDashboardSummary = SampleDashboardData.tuitionSummary,
    recentPayments: List<RecentPaymentActivity> = SampleDashboardData.recentPayments,
    announcements: List<CampusAnnouncement> = SampleDashboardData.announcements,
    unreadNotificationCount: Int = 3,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToTuition: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPayment: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) RuppDarkBg else LightBackground
    val cardBg = if (isDark) RuppCardBg else LightSurface
    val textPrimary = if (isDark) RuppTextPrimary else Color(0xFF1F2937)
    val textSecondary = if (isDark) RuppTextSecondary else Color(0xFF6B7280)
    val borderColor = if (isDark) RuppCardBorder else Color(0xFFE2E8F0)
    val accentGreen = if (isDark) RuppLightGreen else GreenPrimary

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Dashboard Top Header: Student Greeting & Notification Bell
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tap avatar to view profile
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(RuppPrimaryGreen.copy(alpha = 0.2f))
                            .border(1.5.dp, RuppLightGreen, CircleShape)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.initials,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = RuppLightGreen
                        )
                    }

                    Column {
                        Text(
                            text = "Good morning, ${student.fullName.split(" ").lastOrNull() ?: student.fullName} 👋",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "${student.major} • Year ${student.year}",
                            fontSize = 12.sp,
                            color = textSecondary
                        )
                    }
                }

                // Notification Bell Icon with Badge
                IconButton(
                    onClick = onNavigateToNotifications,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isDark) RuppCardElevated else Color(0xFFE5E7EB))
                ) {
                    if (unreadNotificationCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = RuppKhqrRed,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = "$unreadNotificationCount",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = textPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // 2. Active Term Indicator Banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xFF14241B) else Color(0xFFE8F5E9))
                    .border(
                        1.dp,
                        if (isDark) RuppCardBorderSubtle else Color(0xFFC8E6C9),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Academic Term",
                        tint = accentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Term: ${summary.academicYear} • ${summary.semesterName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accentGreen
                    )
                }

                Text(
                    text = "Shift: ${student.shift}",
                    fontSize = 11.sp,
                    color = textSecondary
                )
            }
        }

        // 3. Student Profile Summary & Tuition Card
        item {
            StudentProfileSummaryCard(
                student = student,
                summary = summary,
                onCardClick = onNavigateToProfile,
                onViewBreakdownClick = onNavigateToTuition
            )
        }

        // 4. Quick Action Hub
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Quick Services",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Pay Tuition",
                        subtitle = "KHQR & ABA",
                        icon = Icons.Default.QrCodeScanner,
                        iconTint = RuppLightGreen,
                        iconBg = if (isDark) Color(0xFF122E21) else Color(0xFFDCFCE7),
                        isDark = isDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToPayment
                    )

                    QuickActionCard(
                        title = "Fee Breakdown",
                        subtitle = "Tuition rates",
                        icon = Icons.Default.AccountBalanceWallet,
                        iconTint = Color(0xFF38BDF8),
                        iconBg = if (isDark) Color(0xFF0C243B) else Color(0xFFE0F2FE),
                        isDark = isDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTuition
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Payment History",
                        subtitle = "Past records",
                        icon = Icons.Default.History,
                        iconTint = Color(0xFFFBBF24),
                        iconBg = if (isDark) Color(0xFF2C220E) else Color(0xFFFEF3C7),
                        isDark = isDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHistory
                    )

                    QuickActionCard(
                        title = "Official Receipts",
                        subtitle = "Download PDF",
                        icon = Icons.AutoMirrored.Filled.ReceiptLong,
                        iconTint = Color(0xFFA78BFA),
                        iconBg = if (isDark) Color(0xFF26193E) else Color(0xFFF3E8FF),
                        isDark = isDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHistory
                    )
                }
            }
        }

        // 5. Recent Activity Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    TextButton(onClick = onNavigateToHistory) {
                        Text(
                            text = "See All",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentGreen
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recentPayments.forEach { activity ->
                        RecentTransactionRow(
                            activity = activity,
                            isDark = isDark,
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            onClick = onNavigateToHistory
                        )
                    }
                }
            }
        }

        // 6. University Announcements Banner
        if (announcements.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Campus Bulletins",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    announcements.forEach { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            isDark = isDark,
                            cardBg = cardBg,
                            borderColor = borderColor,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary,
                            accentGreen = accentGreen
                        )
                    }
                }
            }
        }

        // 7. Student Helpdesk Assistance Card
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDark) RuppCardElevated else Color(0xFFF3F4F6))
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                        contentDescription = "Help",
                        tint = accentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Need Assistance with Tuition?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Contact RUPP Finance Office: 023 883 640 or visit STEM Building Rm 102.",
                        fontSize = 11.sp,
                        color = textSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

/**
 * Reusable Quick Action Card.
 */
@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cardBg = if (isDark) RuppCardBg else LightSurface
    val textPrimary = if (isDark) RuppTextPrimary else Color(0xFF1F2937)
    val textSecondary = if (isDark) RuppTextSecondary else Color(0xFF6B7280)
    val borderColor = if (isDark) RuppCardBorder else Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = textSecondary
                )
            }
        }
    }
}

/**
 * Recent Transaction Item Card.
 */
@Composable
private fun RecentTransactionRow(
    activity: RecentPaymentActivity,
    isDark: Boolean,
    cardBg: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0xFF142B21) else Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = "Receipt",
                        tint = RuppAccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = activity.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = activity.paymentMethod,
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                        Text(
                            text = activity.date,
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+$${String.format("%.2f", activity.amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuppAccentGreen
                )
                Text(
                    text = activity.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuppAccentGreen
                )
            }
        }
    }
}

/**
 * Announcement Card.
 */
@Composable
private fun AnnouncementCard(
    announcement: CampusAnnouncement,
    isDark: Boolean,
    cardBg: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    accentGreen: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (announcement.isUrgent) Color(0xFF332010) else Color(0xFF152A20))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = announcement.badgeTag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (announcement.isUrgent) Color(0xFFFBBF24) else accentGreen
                    )
                }

                Text(
                    text = announcement.date,
                    fontSize = 11.sp,
                    color = textSecondary
                )
            }

            Text(
                text = announcement.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Text(
                text = announcement.description,
                fontSize = 11.sp,
                color = textSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun HomeScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        HomeScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F9)
@Composable
fun HomeScreenLightPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        HomeScreen()
    }
}
