package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
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

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun PaymentHistoryFilterSectionPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        PaymentHistoryFilterSection(
            selectedCategory = PaymentCategory.ALL,
            selectedStatus = null,
            searchQuery = "",
            isSearchActive = true,
            onCategorySelected = {},
            onStatusSelected = {},
            onSearchQueryChange = {}
        )
    }
}

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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sleek Animated Search Field
        AnimatedVisibility(
            visible = isSearchActive,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RuppCardElevated)
                    .border(
                        width = 1.dp,
                        color = if (searchQuery.isNotEmpty()) RuppPrimaryGreen.copy(alpha = 0.6f) else RuppCardBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (searchQuery.isNotEmpty()) RuppLightGreen else RuppTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search by title, invoice #, ref code...",
                                color = RuppTextMuted,
                                fontSize = 13.5.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = RuppTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            cursorBrush = SolidColor(RuppPrimaryGreen),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(RuppCardBg)
                                .clickable { onSearchQueryChange("") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = RuppTextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
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
            PaymentCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category

                val chipBgColor by animateColorAsState(
                    targetValue = if (isSelected) RuppPrimaryGreen else RuppCardBg,
                    animationSpec = tween(150),
                    label = "CategoryChipBg"
                )

                val chipBorderColor by animateColorAsState(
                    targetValue = if (isSelected) RuppLightGreen else RuppCardBorder,
                    animationSpec = tween(150),
                    label = "CategoryChipBorder"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(chipBgColor)
                        .border(1.dp, chipBorderColor, RoundedCornerShape(14.dp))
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

        // Minimal Status Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label tag for Status
            Text(
                text = "Status:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuppTextMuted,
                modifier = Modifier.padding(end = 2.dp)
            )

            // "All" Pill
            val isAllStatus = selectedStatus == null
            FilterStatusPill(
                label = "All",
                isSelected = isAllStatus,
                activeColor = RuppLightGreen,
                activeBg = RuppCardElevated,
                onClick = { onStatusSelected(null) }
            )

            // "Completed" Pill
            val isCompleted = selectedStatus == PaymentStatus.COMPLETED
            FilterStatusPill(
                label = "Completed",
                isSelected = isCompleted,
                activeColor = RuppStatusSuccess,
                activeBg = RuppStatusSuccess.copy(alpha = 0.15f),
                dotColor = RuppStatusSuccess,
                onClick = { onStatusSelected(PaymentStatus.COMPLETED) }
            )

            // "Pending" Pill
            val isPending = selectedStatus == PaymentStatus.PENDING
            FilterStatusPill(
                label = "Pending",
                isSelected = isPending,
                activeColor = RuppStatusPending,
                activeBg = RuppStatusPending.copy(alpha = 0.15f),
                dotColor = RuppStatusPending,
                onClick = { onStatusSelected(PaymentStatus.PENDING) }
            )

            // "Failed" Pill
            val isFailed = selectedStatus == PaymentStatus.FAILED
            FilterStatusPill(
                label = "Failed",
                isSelected = isFailed,
                activeColor = RuppStatusFailed,
                activeBg = RuppStatusFailed.copy(alpha = 0.15f),
                dotColor = RuppStatusFailed,
                onClick = { onStatusSelected(PaymentStatus.FAILED) }
            )
        }
    }
}

@Composable
private fun FilterStatusPill(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    activeBg: Color,
    dotColor: Color? = null,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) activeBg else Color.Transparent,
        animationSpec = tween(150),
        label = "StatusPillBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else RuppCardBorder.copy(alpha = 0.5f),
        animationSpec = tween(150),
        label = "StatusPillBorder"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) dotColor else RuppTextMuted.copy(alpha = 0.6f))
                )
            }

            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) activeColor else RuppTextMuted
            )
        }
    }
}
