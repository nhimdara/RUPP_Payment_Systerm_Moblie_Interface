package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FeeStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleStudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.StudentProfile
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.FeeRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.SchoolFeeCard

enum class FeeFilterTab(val label: String) {
    ALL("All Fees"),
    TO_PAY("To Pay"),
    PAID("Paid")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolFeeListScreen(
    student: StudentProfile = SampleStudentProfile.sokha,
    isDarkMode: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onFeeClick: (SchoolFeeItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(FeeFilterTab.ALL) }
    val allFees = remember { FeeRepository.getAllFees() }

    val filteredFees = when (selectedTab) {
        FeeFilterTab.ALL -> allFees
        FeeFilterTab.TO_PAY -> allFees.filter { it.status == FeeStatus.UNPAID || it.status == FeeStatus.FAILED }
        FeeFilterTab.PAID -> allFees.filter { it.status == FeeStatus.PAID }
    }

    val totalSemAmount = allFees.sumOf { it.totalAmount }
    val totalToPay = FeeRepository.getTotalToPayAmount()

    val screenBg = if (isDarkMode) Color(0xFF0E1411) else Color(0xFFF9F8F4)
    val cardBg = if (isDarkMode) Color(0xFF161F1B) else Color.White
    val cardBorder = if (isDarkMode) Color(0xFF24322B) else Color(0xFFE5E7EB)
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1F2937)
    val textSecondary = if (isDarkMode) Color(0xFF90A198) else Color(0xFF6B7280)
    val activePillBg = if (isDarkMode) Color(0xFF1E3F32) else Color(0xFF1E3F32)
    val activePillText = Color.White

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(screenBg)
            .statusBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(cardBg)
                    .border(1.dp, cardBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = "Academic Fee Structure",
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Year ${student.academicYear} ❖ Semester ${student.semester}",
                    color = textSecondary,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 6.dp,
                bottom = 40.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Faculty Affiliation & Summary Card (Dynamic from student profile)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDarkMode) Color(0xFF1E3F32) else Color(0xFFE6F4EA))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = student.faculty.uppercase(),
                                color = if (isDarkMode) Color(0xFF34D399) else Color(0xFF137333),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = student.major,
                            color = textPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Department of ${student.department}",
                            color = textSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SummaryStatBox(
                                label = "Total Credits",
                                value = "18 Credits",
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Remaining",
                                value = "$${totalToPay.toInt()}",
                                valueColor = Color(0xFFDC2626),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Semester Total",
                                value = "$${totalSemAmount.toInt()}",
                                valueColor = if (isDarkMode) Color(0xFF34D399) else Color(0xFF059669),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Segmented Filter Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(1.dp, cardBorder, RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FeeFilterTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) activePillBg else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.label,
                                color = if (isSelected) activePillText else textSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 3. Itemized Fee List
            items(filteredFees) { fee ->
                SchoolFeeCard(
                    item = fee,
                    isDarkMode = isDarkMode,
                    onClick = onFeeClick
                )
            }
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    value: String,
    valueColor: Color? = null,
    cardBg: Color,
    cardBorder: Color,
    textPrimary: Color,
    textSecondary: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = textSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = valueColor ?: textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

