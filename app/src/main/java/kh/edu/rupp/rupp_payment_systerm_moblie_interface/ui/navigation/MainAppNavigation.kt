package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentMethodType
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentReceipt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionDashboardSummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionPaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentModal
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.ReceiptModal
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.RuppBottomNavBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.RuppNavTab
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.StudentProfileSummaryCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.TopUpModal
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.CampusCardTab
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.FinancialAnalyticsScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.HomeScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.NotificationsScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.SchoolFeeListScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.SmartCampusCardsScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.profile.StudentProfileScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenAccentDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAccentGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardElevated
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableDoubleStateOf

/**
 * Destinations for secondary or detail screens.
 */
enum class SubDestination {
    NOTIFICATIONS,
    CAMPUS_CARDS,
    FINANCIAL_ANALYTICS
}

/**
 * Master App Navigation Component.
 * Orchestrates seamless switching between Home, Tuition, History, and Profile tabs,
 * handles child screen transitions (such as Notifications, Campus Cards, and Financial Analytics),
 * and manages Android system back events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation(
    initialTab: RuppNavTab = RuppNavTab.HOME,
    student: StudentProfile = SampleStudentProfile.student,
    summary: TuitionDashboardSummary = SampleDashboardData.tuitionSummary,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    var currentSubDestination by remember { mutableStateOf<SubDestination?>(null) }
    var notifications by remember { mutableStateOf(SampleDashboardData.initialNotifications) }
    var selectedFee by remember { mutableStateOf<SchoolFeeItem?>(null) }
    var showPaymentModal by remember { mutableStateOf(false) }
    var activeReceipt by remember { mutableStateOf<PaymentReceipt?>(null) }
    var showTopUpModal by remember { mutableStateOf(false) }
    var studentBalance by remember { mutableDoubleStateOf(student.balance) }
    var selectedCampusCardTab by remember { mutableStateOf(CampusCardTab.STUDENT_ID) }
    var isDarkMode by remember { mutableStateOf(false) }

    val unreadCount = notifications.count { !it.isRead }

    // Intercept system back press: return from subscreen or return to Home tab
    BackHandler(enabled = currentSubDestination != null || selectedTab != RuppNavTab.HOME) {
        if (currentSubDestination != null) {
            currentSubDestination = null
        } else if (selectedTab != RuppNavTab.HOME) {
            selectedTab = RuppNavTab.HOME
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Keep bottom bar visible on root tabs, hide on standalone screens like Notifications, Campus Cards, Analytics
            if (currentSubDestination == null) {
                RuppBottomNavBar(
                    selectedTab = selectedTab,
                    unreadNotificationCount = unreadCount,
                    isDarkMode = isDarkMode,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        currentSubDestination = null
                    }
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentSubDestination ?: selectedTab,
            label = "screen_crossfade"
        ) { destination ->
            when (destination) {
                SubDestination.NOTIFICATIONS -> {
                    NotificationsScreen(
                        onNavigateBack = { currentSubDestination = null },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                SubDestination.CAMPUS_CARDS -> {
                    SmartCampusCardsScreen(
                        student = student,
                        initialTab = selectedCampusCardTab,
                        onNavigateBack = { currentSubDestination = null },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                SubDestination.FINANCIAL_ANALYTICS -> {
                    FinancialAnalyticsScreen(
                        onNavigateBack = { currentSubDestination = null },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                RuppNavTab.HOME -> {
                    HomeScreen(
                        student = student,
                        summary = summary,
                        studentBalance = studentBalance,
                        unreadNotificationCount = unreadCount,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = !isDarkMode },
                        onNavigateToNotifications = {
                            currentSubDestination = SubDestination.NOTIFICATIONS
                        },
                        onNavigateToTuition = {
                            selectedTab = RuppNavTab.TUITION
                        },
                        onNavigateToHistory = {
                            selectedTab = RuppNavTab.HISTORY
                        },
                        onNavigateToProfile = {
                            selectedTab = RuppNavTab.PROFILE
                        },
                        onNavigateToPayment = {
                            selectedTab = RuppNavTab.TUITION
                        },
                        onNavigateToCampusCards = { tabName ->
                            selectedCampusCardTab = when {
                                tabName.contains("Moto", ignoreCase = true) -> CampusCardTab.MOTO_PARK
                                tabName.contains("Library", ignoreCase = true) -> CampusCardTab.LIBRARY_PASS
                                else -> CampusCardTab.STUDENT_ID
                            }
                            currentSubDestination = SubDestination.CAMPUS_CARDS
                        },
                        onNavigateToAnalytics = {
                            currentSubDestination = SubDestination.FINANCIAL_ANALYTICS
                        },
                        onOpenTopUp = {
                            showTopUpModal = true
                        },
                        onViewReceipt = { receipt ->
                            activeReceipt = receipt
                        },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                RuppNavTab.TUITION -> {
                    SchoolFeeListScreen(
                        student = student,
                        isDarkMode = isDarkMode,
                        onNavigateBack = {
                            selectedTab = RuppNavTab.HOME
                        },
                        onFeeClick = { fee ->
                            selectedFee = fee
                            showPaymentModal = true
                        },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                RuppNavTab.HISTORY -> {
                    HistoryTabContent(
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                RuppNavTab.PROFILE -> {
                    StudentProfileScreen(
                        student = student,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { isDarkMode = it },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }

                else -> {}
            }
        }
    }

    // Top Up Student Balance Modal Bottom Sheet (Image 5)
    if (showTopUpModal) {
        TopUpModal(
            currentBalance = studentBalance,
            onDismiss = { showTopUpModal = false },
            onTopUpConfirmed = { amount ->
                studentBalance += amount
                showTopUpModal = false
            }
        )
    }

    // Modal Checkout: Tab Ayutaya's Payment Method Selection & Balance Validation
    if (showPaymentModal && selectedFee != null) {
        PaymentModal(
            feeItem = selectedFee!!,
            studentBalance = studentBalance,
            onDismiss = {
                showPaymentModal = false
            },
            onConfirmPayment = { fee, method ->
                showPaymentModal = false
                if (method == PaymentMethodType.STUDENT_WALLET) {
                    studentBalance = (studentBalance - fee.totalAmount).coerceAtLeast(0.0)
                }
                val timestamp = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
                val randomReceiptId = (1000..9999).random()

                activeReceipt = PaymentReceipt(
                    receiptNumber = "RCP-2026-$randomReceiptId",
                    studentName = student.fullName,
                    studentId = student.studentId,
                    facultyName = student.faculty,
                    majorName = student.major,
                    feeTitle = fee.title,
                    totalAmount = fee.totalAmount,
                    paymentMethod = method,
                    transactionRef = "TXN-${System.currentTimeMillis().toString().takeLast(8)}",
                    issueDate = timestamp
                )
            }
        )
    }

    // Official Electronic Receipt Modal (pops up immediately upon successful confirmation)
    activeReceipt?.let { receipt ->
        ReceiptModal(
            receipt = receipt,
            onDismiss = {
                activeReceipt = null
            }
        )
    }
}

/**
 * Tuition & Fee Breakdown Screen presentation for the TUITION tab.
 */
@Composable
private fun TuitionTabContent(
    student: StudentProfile,
    summary: TuitionDashboardSummary,
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
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Tuition & Fee Ledger",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Faculty of Engineering • ${student.major}",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        }

        // Summary Card
        item {
            StudentProfileSummaryCard(
                student = student,
                summary = summary
            )
        }

        // Breakdown Items
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Fee Structure Breakdown",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                HorizontalDivider(color = borderColor)

                FeeLineItem("Academic Tuition Fee", "$500.00", textPrimary, textSecondary)
                FeeLineItem("STEM & Telecom Lab Access", "$70.00", textPrimary, textSecondary)
                FeeLineItem("Digital Library & Software License", "$25.00", textPrimary, textSecondary)
                FeeLineItem("Student Welfare & Identity Service", "$5.00", textPrimary, textSecondary)

                HorizontalDivider(color = borderColor)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Term Cost",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "$600.00",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentGreen
                    )
                }
            }
        }

        // Supported Payment Channels
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Available Payment Channels",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                PaymentMethodChip("KHQR Bakong", "Instant verification via mobile banking", accentGreen, isDark)
                PaymentMethodChip("ABA PayWay", "Direct in-app checkout", Color(0xFF38BDF8), isDark)
                PaymentMethodChip("Wing Bank / ACLEDA", "Branch slip & counter deposit", Color(0xFFFBBF24), isDark)
            }
        }
    }
}

@Composable
private fun FeeLineItem(
    title: String,
    amount: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 13.sp, color = textSecondary)
        Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
    }
}

@Composable
private fun PaymentMethodChip(
    title: String,
    subtitle: String,
    accent: Color,
    isDark: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDark) Color(0xFF14241B) else Color(0xFFF9FAFB))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
            Text(text = subtitle, fontSize = 10.sp, color = Color(0xFF9CA3AF))
        }
    }
}

/**
 * Payment History Screen presentation for the HISTORY tab.
 */
@Composable
private fun HistoryTabContent(
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
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Payment Records & Receipts",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Official certified receipts from RUPP Finance",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        }

        // Summary Statistics Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) Color(0xFF152A20) else Color(0xFFDCFCE7))
                    .border(1.dp, if (isDark) RuppCardBorder else Color(0xFF86EFAC), RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total Payments Confirmed", fontSize = 11.sp, color = textSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "$650.00", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = accentGreen)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(accentGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "3 Invoices Settled", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentGreen)
                    }
                }
            }
        }

        items(SampleDashboardData.recentPayments) { payment ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
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
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(RuppPrimaryGreen.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = "Receipt",
                                tint = accentGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(text = payment.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text(text = "${payment.receiptNumber} • ${payment.paymentMethod}", fontSize = 11.sp, color = textSecondary)
                            Text(text = payment.date, fontSize = 10.sp, color = textSecondary)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+$${String.format("%.2f", payment.amount)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentGreen
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF142B21))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = payment.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = accentGreen)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Student Profile & Academic Details screen presentation for the PROFILE tab.
 */
@Composable
private fun ProfileTabContent(
    student: StudentProfile,
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
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(RuppPrimaryGreen.copy(alpha = 0.2f))
                        .border(2.dp, accentGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.initials,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentGreen
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = student.fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                if (student.khmerName.isNotBlank()) {
                    Text(
                        text = student.khmerName,
                        fontSize = 13.sp,
                        color = textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(ActiveMint.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "ENROLLED • ID: ${student.studentId.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ActiveMint
                    )
                }
            }
        }

        // Academic Record Details
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Academic Record",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                HorizontalDivider(color = borderColor)

                ProfileRow("Faculty", student.faculty, textPrimary, textSecondary)
                ProfileRow("Major", student.major, textPrimary, textSecondary)
                ProfileRow("Classification", "Year ${student.year}, Semester ${student.semester}", textPrimary, textSecondary)
                ProfileRow("Academic Term", student.academicYear, textPrimary, textSecondary)
                ProfileRow("Study Shift", student.shift, textPrimary, textSecondary)
                ProfileRow("Email", student.email, textPrimary, textSecondary)
                ProfileRow("Phone", student.phone, textPrimary, textSecondary)
            }
        }

        // Account Options
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Account & Preferences",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                HorizontalDivider(color = borderColor)

                ProfileSettingItem("Change Password", Icons.Default.Lock, textPrimary, textSecondary)
                ProfileSettingItem("Language (ភាសាខ្មែរ / English)", Icons.Default.Translate, textPrimary, textSecondary)
                ProfileSettingItem("Notification Alerts", Icons.Default.Notifications, textPrimary, textSecondary)
            }
        }
    }
}

@Composable
private fun ProfileRow(
    label: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = textSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
    }
}

@Composable
private fun ProfileSettingItem(
    title: String,
    icon: ImageVector,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = textSecondary, modifier = Modifier.size(18.dp))
            Text(text = title, fontSize = 13.sp, color = textPrimary)
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = textSecondary, modifier = Modifier.size(12.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun MainAppNavigationPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        MainAppNavigation()
    }
}
