package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentMethodType
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentReceipt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionDashboardSummary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.FeeRepository

@Composable
fun HomeScreen(
    student: StudentProfile = SampleStudentProfile.sokha,
    summary: TuitionDashboardSummary = SampleDashboardData.tuitionSummary,
    studentBalance: Double = 145.00,
    unreadNotificationCount: Int = 3,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToTuition: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPayment: () -> Unit = {},
    onNavigateToCampusCards: (String) -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onOpenTopUp: () -> Unit = {},
    onViewReceipt: (PaymentReceipt) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val toPayFees = FeeRepository.getToPayFees()
    val totalToPay = FeeRepository.getTotalToPayAmount()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F8F4))
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Header: Good morning Sokha, Dark/Light mode, Student View, Bell, Avatar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = student.fullName.split(" ").firstOrNull() ?: "Sokha",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Theme Mode Toggle
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Theme",
                            tint = Color(0xFF4B5563),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Student View Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Student View",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                    }

                    // Notification Bell with Badge
                    IconButton(
                        onClick = onNavigateToNotifications,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981))
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF4B5563),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }

                    // SC Avatar Circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFE9DC))
                            .border(1.dp, Color(0xFFD7CCC8), CircleShape)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.initials,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                    }
                }
            }
        }

        // 2. Hero Card: RUPP Student ID Card
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1B3D2F),
                                    Color(0xFF152C23),
                                    Color(0xFF0F211A)
                                )
                            )
                        )
                        .clickable { onNavigateToCampusCards("Student ID") }
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Header of ID Card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.95f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.rupp_logo),
                                        contentDescription = "RUPP Seal",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "ROYAL UNIVERSITY OF PHNOM PENH",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.9f),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ",
                                        fontSize = 9.sp,
                                        color = Color.White.copy(alpha = 0.65f)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF2C4A3C))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "STUDENT CARD",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        // Middle: Photo placeholder + Student Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFEDE9DE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = student.fullName,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Text(
                                    text = student.faculty,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )

                                Text(
                                    text = "ID ${student.studentId}",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Bottom: Barcode and Academic Year
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(20.dp)
                            ) {
                                BarcodeGraphic()
                            }

                            Text(
                                text = student.academicYear,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Subtitle link: Click card to show campus QR barcode
                Text(
                    text = "📇 Click card to show campus QR barcode",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.clickable { onNavigateToCampusCards("Student ID") }
                )
            }
        }

        // 3. Student Balance Card with "+ Top up"
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(18.dp))
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Student balance",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$${String.format("%.2f", studentBalance)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E3F32))
                            .clickable { onOpenTopUp() }
                            .padding(horizontal = 16.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ Top up",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 4. Quick Actions Hub (4 square items)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    modifier = Modifier.weight(1f),
                    title = "Pay tuition",
                    icon = Icons.Default.CreditCard,
                    onClick = onNavigateToTuition
                )
                QuickActionItem(
                    modifier = Modifier.weight(1f),
                    title = "Receipts",
                    icon = Icons.Default.Bookmark,
                    onClick = onNavigateToHistory
                )
                QuickActionItem(
                    modifier = Modifier.weight(1f),
                    title = "History",
                    icon = Icons.Default.History,
                    onClick = onNavigateToHistory
                )
                QuickActionItem(
                    modifier = Modifier.weight(1f),
                    title = "Fee plan",
                    icon = Icons.Default.BarChart,
                    onClick = onNavigateToAnalytics
                )
            }
        }

        // 5. "To pay" Section (3 items matching Image 1)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "To pay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Total $${String.format("%.2f", totalToPay)}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // 3 To Pay Fee Cards
                toPayFees.take(3).forEach { fee ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                            .clickable { onNavigateToTuition() }
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
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF3EFE6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = Color(0xFF8D6E63),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = fee.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Text(
                                        text = fee.subtitle,
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$${String.format("%.2f", fee.totalAmount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )

                                val isRetry = fee.subtitle.contains("failed", ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isRetry) Color(0xFFFEE2E2) else Color(0xFFFEF3C7))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isRetry) "Retry" else "Unpaid",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isRetry) Color(0xFFDC2626) else Color(0xFFB45309)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. RUPP MOTO PARK CARD Deep Blue Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF133659))
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
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDE3618)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TwoWheeler,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "RUPP MOTO PARK CARD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Pass Active - Plate ${student.motoPlate}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.clickable { onNavigateToCampusCards("Moto Park Card") },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Manage Card >",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 7. RECENT RECEIPTS Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT RECEIPTS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "View all",
                        fontSize = 12.sp,
                        color = Color(0xFF1E3F32),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToHistory() }
                    )
                }

                // Receipt 1 (Matches Image 6 Official Electronic Receipt)
                RecentReceiptItem(
                    title = "Laboratory & Library Fee - 2026–2027 (Semester 1)",
                    ref = "RUPP-RCPT-2026-8076 · 2026-10-26 12:35:39",
                    amount = "+$25.00",
                    onClick = {
                        onViewReceipt(
                            PaymentReceipt(
                                receiptNumber = "RUPP-RCPT-2026-8076",
                                studentName = student.fullName,
                                studentId = student.studentId,
                                facultyName = student.faculty,
                                majorName = student.major,
                                feeTitle = "Laboratory & Library Fee - 2026–2027 (Semester 1)",
                                totalAmount = 25.00,
                                paymentMethod = PaymentMethodType.STUDENT_WALLET,
                                transactionRef = "RUPP-TXN-1788819",
                                issueDate = "2026-10-26 12:35:39"
                            )
                        )
                    }
                )

                // Receipt 2
                RecentReceiptItem(
                    title = "Entrance & Placement Test Fee (Academic Year 2026-2027)",
                    ref = "RUPP-RCPT-2026-0413 · 2026-09-11 09:15:00",
                    amount = "+$150.00",
                    onClick = {
                        onViewReceipt(
                            PaymentReceipt(
                                receiptNumber = "RUPP-RCPT-2026-0413",
                                studentName = student.fullName,
                                studentId = student.studentId,
                                facultyName = student.faculty,
                                majorName = student.major,
                                feeTitle = "Entrance & Placement Test Fee (Academic Year 2026-2027)",
                                totalAmount = 150.00,
                                paymentMethod = PaymentMethodType.KHQR_BAKONG,
                                transactionRef = "RUPP-TXN-1652490",
                                issueDate = "2026-09-11 09:15:00"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFE8ECE9))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF1E3F32),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RecentReceiptItem(
    title: String,
    ref: String,
    amount: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE6F4EA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF137333),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(modifier = Modifier.padding(end = 8.dp)) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = ref,
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = amount,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE6F4EA))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Verified",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF137333)
                    )
                }
            }
        }
    }
}
