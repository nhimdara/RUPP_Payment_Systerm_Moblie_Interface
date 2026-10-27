package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardElevated
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusFailed
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusPending
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusSuccess
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary

@Composable
fun PaymentHistoryFilterSection(
    selectedCategory: PaymentCategory,
    selectedStatus: PaymentStatus?,
    searchQuery: String,
    isSearchActive: Boolean,
    onCategorySelected: (PaymentCategory) -> Unit,
    onStatusSelected: (PaymentStatus?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Expandable Search Field
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RuppCardElevated)
                    .border(1.dp, RuppCardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = RuppTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search tuition, exam, ref number...",
                                color = RuppTextMuted,
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = RuppTextPrimary,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(RuppPrimaryGreen),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = RuppTextSecondary,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onSearchQueryChange("") }
                        )
                    }
                }
            }
        }

        // Horizontal Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PaymentCategory.values().forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) RuppPrimaryGreen else RuppCardBg
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) RuppLightGreen else RuppCardBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.displayName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) RuppTextPrimary else RuppTextSecondary
                    )
                }
            }
        }

        // Status Filter Chips (All, Completed, Pending, Failed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "All Status" pill
            val isAllStatus = selectedStatus == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isAllStatus) RuppCardElevated else Color.Transparent)
                    .border(
                        1.dp,
                        if (isAllStatus) RuppPrimaryGreen.copy(alpha = 0.5f) else RuppCardBorder.copy(alpha = 0.6f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onStatusSelected(null) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "All Status",
                    fontSize = 11.sp,
                    color = if (isAllStatus) RuppLightGreen else RuppTextMuted,
                    fontWeight = FontWeight.Medium
                )
            }

            // Completed
            val isCompleted = selectedStatus == PaymentStatus.COMPLETED
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isCompleted) RuppStatusSuccess.copy(alpha = 0.15f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isCompleted) RuppStatusSuccess else RuppCardBorder.copy(alpha = 0.6f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onStatusSelected(PaymentStatus.COMPLETED) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RuppStatusSuccess)
                    )
                    Text(
                        text = "Completed",
                        fontSize = 11.sp,
                        color = if (isCompleted) RuppStatusSuccess else RuppTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Pending
            val isPending = selectedStatus == PaymentStatus.PENDING
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isPending) RuppStatusPending.copy(alpha = 0.15f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isPending) RuppStatusPending else RuppCardBorder.copy(alpha = 0.6f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onStatusSelected(PaymentStatus.PENDING) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RuppStatusPending)
                    )
                    Text(
                        text = "Pending",
                        fontSize = 11.sp,
                        color = if (isPending) RuppStatusPending else RuppTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Failed
            val isFailed = selectedStatus == PaymentStatus.FAILED
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isFailed) RuppStatusFailed.copy(alpha = 0.15f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isFailed) RuppStatusFailed else RuppCardBorder.copy(alpha = 0.6f),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onStatusSelected(PaymentStatus.FAILED) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RuppStatusFailed)
                    )
                    Text(
                        text = "Failed",
                        fontSize = 11.sp,
                        color = if (isFailed) RuppStatusFailed else RuppTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
