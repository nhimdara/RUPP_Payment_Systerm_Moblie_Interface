package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAvatarBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppAvatarText
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppStatusSuccess
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun PaymentHistoryTopBarPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        Column {
            PaymentHistoryTopBar(
                title = "Payment History",
                subtitle = "Sokha Chan • ID 20260142",
                isSearchActive = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            PaymentHistoryTopBar(
                title = "Payment History",
                subtitle = "Sokha Chan • ID 20260142",
                isSearchActive = true
            )
        }
    }
}

@Composable
fun PaymentHistoryTopBar(
    modifier: Modifier = Modifier,
    title: String = "Payment History",
    subtitle: String = "Sokha Chan • ID 20260142",
    isSearchActive: Boolean = false,
    onBackClick: () -> Unit = {},
    onSearchToggle: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    val searchBgColor by animateColorAsState(
        targetValue = if (isSearchActive) RuppPrimaryGreen.copy(alpha = 0.18f) else RuppCardBg,
        animationSpec = tween(durationMillis = 200),
        label = "SearchBgAnimation"
    )

    val searchBorderColor by animateColorAsState(
        targetValue = if (isSearchActive) RuppLightGreen else RuppCardBorder,
        animationSpec = tween(durationMillis = 200),
        label = "SearchBorderAnimation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RuppDarkBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sleek Back Button with border matching card elements
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RuppCardBg)
                    .border(1.dp, RuppCardBorder, RoundedCornerShape(14.dp))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RuppTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title and Student Subtitle
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuppTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(RuppStatusSuccess)
                    )

                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = RuppTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Action Buttons: Search Toggle & Student Avatar Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Search Toggle Button with animated active indicator
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(searchBgColor)
                        .border(1.dp, searchBorderColor, RoundedCornerShape(14.dp))
                        .clickable { onSearchToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = if (isSearchActive) "Close Search" else "Search",
                        tint = if (isSearchActive) RuppLightGreen else RuppTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Student Avatar Badge ("SC" for Sokha Chan with active green ring)
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(RuppAvatarBg)
                            .border(1.5.dp, RuppPrimaryGreen.copy(alpha = 0.6f), CircleShape)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SC",
                            color = RuppAvatarText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Online/Verified status indicator dot on bottom right of avatar
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(RuppStatusSuccess)
                            .border(1.5.dp, RuppDarkBg, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }

        // Subtle bottom separation line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RuppCardBorder.copy(alpha = 0.5f))
        )
    }
}
