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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.FeeRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.SchoolFeeCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActionIconBgDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkSurface
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppEmeraldLight
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTopUpGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextLightGray
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.TextWhite

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    studentBalance: Double = 120.00,
    onTopUpClick: () -> Unit = {},
    onPayTuitionClick: () -> Unit = {},
    onReceiptsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onFeePlanClick: () -> Unit = {},
    onFeeItemClick: (SchoolFeeItem) -> Unit = {}
) {
    val toPayFees = remember { FeeRepository.getToPayFees() }
    val totalToPay = remember { FeeRepository.getTotalToPayAmount() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 48.dp,   // Space below the status bar
            bottom = 80.dp // Comfortable space to scroll past the bottom bar
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Student Balance Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(26.dp))
                    .padding(horizontal = 22.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Student balance",
                            color = TextLightGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("$%.2f", studentBalance),
                            color = TextWhite,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // + Top up button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(RuppTopUpGreen)
                            .clickable { onTopUpClick() }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Top up",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Top up",
                                color = TextWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. Quick Action Grid (Pay tuition, Receipts, History, Fee plan)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Default.CreditCard,
                    label = "Pay tuition",
                    onClick = onPayTuitionClick
                )
                QuickActionButton(
                    icon = Icons.Default.Bookmark,
                    label = "Receipts",
                    onClick = onReceiptsClick
                )
                QuickActionButton(
                    icon = Icons.Default.History,
                    label = "History",
                    onClick = onHistoryClick
                )
                QuickActionButton(
                    icon = Icons.Default.BarChart,
                    label = "Fee plan",
                    onClick = onFeePlanClick
                )
            }
        }

        // 3. "To pay" Section Header
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "To pay",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = String.format("Total $%.2f", totalToPay),
                    color = TextLightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 4. List of Fees to Pay
        items(toPayFees) { feeItem ->
            SchoolFeeCard(
                item = feeItem,
                onClick = onFeeItemClick
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(ActionIconBgDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = RuppEmeraldLight,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = TextLightGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
