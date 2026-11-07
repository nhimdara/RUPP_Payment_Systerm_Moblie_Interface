package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AppNotification
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.NotificationCategory
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SampleDashboardData
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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardElevated
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary

/**
 * Notifications Screen.
 * Provides filtered real-time alerts for payment receipts, tuition deadlines, and university news.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) RuppDarkBg else LightBackground
    val cardBg = if (isDark) RuppCardBg else LightSurface
    val textPrimary = if (isDark) RuppTextPrimary else Color(0xFF1F2937)
    val textSecondary = if (isDark) RuppTextSecondary else Color(0xFF6B7280)
    val borderColor = if (isDark) RuppCardBorder else Color(0xFFE2E8F0)
    val accentGreen = if (isDark) RuppLightGreen else GreenPrimary

    var selectedCategory by remember { mutableStateOf(NotificationCategory.ALL) }
    var notifications by remember { mutableStateOf(SampleDashboardData.initialNotifications) }

    val unreadCount = notifications.count { !it.isRead }

    val filteredList = remember(selectedCategory, notifications) {
        if (selectedCategory == NotificationCategory.ALL) {
            notifications
        } else {
            notifications.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textPrimary
                    )
                }

                Text(
                    text = "Notifications",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(RuppPrimaryGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$unreadCount new",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentGreen
                        )
                    }
                }
            }

            if (unreadCount > 0) {
                TextButton(
                    onClick = {
                        notifications = notifications.map { it.copy(isRead = true) }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Mark all as read",
                        tint = accentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Read all",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accentGreen
                    )
                }
            }
        }

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(NotificationCategory.values()) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) {
                                if (isDark) RuppPrimaryGreen.copy(alpha = 0.25f) else GreenPrimary.copy(alpha = 0.15f)
                            } else {
                                if (isDark) RuppCardElevated else Color(0xFFE5E7EB)
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) accentGreen else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) accentGreen else textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Notifications List or Empty State
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(if (isDark) RuppCardElevated else Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Empty",
                            tint = textSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "No Notifications",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Text(
                        text = "You don't have any notifications in ${selectedCategory.label}.",
                        fontSize = 13.sp,
                        color = textSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    NotificationCardItem(
                        item = item,
                        isDark = isDark,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        accentGreen = accentGreen,
                        onClick = {
                            notifications = notifications.map {
                                if (it.id == item.id) it.copy(isRead = true) else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    item: AppNotification,
    isDark: Boolean,
    cardBg: Color,
    borderColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    accentGreen: Color,
    onClick: () -> Unit
) {
    val unreadBg = if (isDark) Color(0xFF13231B) else Color(0xFFF0FDF4)
    val itemBg = if (!item.isRead) unreadBg else cardBg

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(itemBg)
            .border(
                width = 1.dp,
                color = if (!item.isRead) accentGreen.copy(alpha = 0.4f) else borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Category Icon with colored background
            val (icon, iconTint, iconBg) = when (item.category) {
                NotificationCategory.PAYMENTS -> Triple(
                    Icons.Default.CheckCircle,
                    RuppAccentGreen,
                    if (isDark) Color(0xFF11291E) else Color(0xFFDCFCE7)
                )
                NotificationCategory.ACADEMIC -> Triple(
                    Icons.Default.School,
                    Color(0xFF38BDF8),
                    if (isDark) Color(0xFF0C243B) else Color(0xFFE0F2FE)
                )
                NotificationCategory.ANNOUNCEMENTS -> Triple(
                    Icons.Default.Campaign,
                    Color(0xFFFBBF24),
                    if (isDark) Color(0xFF2E220D) else Color(0xFFFEF3C7)
                )
                NotificationCategory.ALL -> Triple(
                    Icons.Default.CheckCircle,
                    accentGreen,
                    if (isDark) Color(0xFF11291E) else Color(0xFFDCFCE7)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.category.label,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Notification Text Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.timeAgo,
                            fontSize = 11.sp,
                            color = textSecondary
                        )

                        // Unread Dot
                        if (!item.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(ActiveMint)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    fontSize = 12.sp,
                    color = textSecondary,
                    lineHeight = 17.sp
                )

                // Optional Payment / Receipt tags
                if (item.amount != null || item.receiptNumber != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.amount != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(RuppAccentGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$${String.format("%.2f", item.amount)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RuppAccentGreen
                                )
                            }
                        }

                        if (item.receiptNumber != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isDark) RuppCardElevated else Color(0xFFE5E7EB))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.receiptNumber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun NotificationsScreenDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        NotificationsScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F9)
@Composable
fun NotificationsScreenLightPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        NotificationsScreen()
    }
}
