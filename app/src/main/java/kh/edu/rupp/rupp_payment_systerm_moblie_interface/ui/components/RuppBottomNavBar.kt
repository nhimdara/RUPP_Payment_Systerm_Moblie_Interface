package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.ActiveMint
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkNavBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenAccentDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.GreenPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightNavBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.LightNavUnselected
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppKhqrRed
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted

/**
 * Main bottom navigation destination tabs for RUPP Tuition Payment App.
 */
enum class RuppNavTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    TUITION("Tuition", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    HISTORY("History", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

/**
 * University-branded Bottom Navigation Bar with badge support, animated indicators,
 * and edge-to-edge support.
 */
@Composable
fun RuppBottomNavBar(
    selectedTab: RuppNavTab = RuppNavTab.HOME,
    onTabSelected: (RuppNavTab) -> Unit = {},
    unreadNotificationCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val navBgColor = if (isDark) RuppDarkBg else LightNavBar
    val borderColor = if (isDark) RuppCardBorder else Color(0xFFE2E8F0)
    val activeColor = if (isDark) RuppLightGreen else GreenPrimary
    val inactiveColor = if (isDark) RuppTextMuted else LightNavUnselected

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(navBgColor)
            .navigationBarsPadding()
    ) {
        // Subtle top divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RuppNavTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val animatedTint by animateColorAsState(
                    targetValue = if (isSelected) activeColor else inactiveColor,
                    animationSpec = tween(durationMillis = 200),
                    label = "nav_tab_tint"
                )

                // Tab Badge display logic
                val currentBadge = when (tab) {
                    RuppNavTab.HOME -> if (unreadNotificationCount > 0 && !isSelected) unreadNotificationCount else null
                    else -> tab.badgeCount
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false, radius = 28.dp),
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    if (currentBadge != null) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = RuppKhqrRed,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (currentBadge > 9) "9+" else "$currentBadge",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                tint = animatedTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label,
                            tint = animatedTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = animatedTint
                    )

                    // Small indicator dot below active tab
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(activeColor)
                        )
                    } else {
                        Box(modifier = Modifier.size(4.dp)) // Maintain fixed spacing height
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun RuppBottomNavBarDarkPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        RuppBottomNavBar(
            selectedTab = RuppNavTab.HOME,
            unreadNotificationCount = 3
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4F9)
@Composable
fun RuppBottomNavBarLightPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = false) {
        RuppBottomNavBar(
            selectedTab = RuppNavTab.TUITION,
            unreadNotificationCount = 2
        )
    }
}
