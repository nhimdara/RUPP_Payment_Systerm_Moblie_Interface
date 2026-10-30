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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.FeeRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.SchoolFeeCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppEmerald
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppForestGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextLightGray
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextWhite

enum class FeeFilterTab(val label: String) {
    ALL("All Fees"),
    TO_PAY("To Pay"),
    PAID("Paid")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolFeeListScreen(
    onNavigateBack: () -> Unit = {},
    onFeeClick: (SchoolFeeItem) -> Unit = {}
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Academic Fee Structure",
                            color = TextWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Year 2026-2027 · Semester 1",
                            color = TextLightGray,
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 40.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Faculty Affiliation & Summary Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(24.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(RuppForestGreen)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "FACULTY OF SCIENCE",
                                color = RuppEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Computer Science & Engineering",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Department of Information Technology",
                            color = TextLightGray,
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
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Remaining",
                                value = String.format("$%.0f", totalToPay),
                                valueColor = Color(0xFFF87171),
                                modifier = Modifier.weight(1f)
                            )
                            SummaryStatBox(
                                label = "Semester Total",
                                value = String.format("$%.0f", totalSemAmount),
                                valueColor = RuppEmerald,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Filter Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FeeFilterTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) RuppForestGreen else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.label,
                                color = if (isSelected) TextWhite else TextLightGray,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 3. Fee List items
            if (filteredFees.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RuppEmerald,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No fees found",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                items(filteredFees) { item ->
                    SchoolFeeCard(
                        item = item,
                        onClick = onFeeClick
                    )
                }
            }

            // Extra bottom spacing so the last item scrolls well above the edge
            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    value: String,
    valueColor: Color = TextWhite,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF1E2A23))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = TextLightGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = valueColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
