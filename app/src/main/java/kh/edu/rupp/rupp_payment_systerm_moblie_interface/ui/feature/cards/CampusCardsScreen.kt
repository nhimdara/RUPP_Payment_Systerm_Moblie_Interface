package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.TwoWheeler
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.DashboardData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.CardSkin
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.HeaderBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.InfoRow
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PillTabs
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SectionTitle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SmartCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SoftButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.cardNumber
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.serviceVisual
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.QrCode

/**
 * RUPP Smart Campus Cards: student ID plus the moto parking and library passes.
 * Pass status comes from GET /service-fees/my (PAID = active card).
 */
@Composable
fun CampusCardsScreen(
    data: DashboardData,
    onBack: () -> Unit,
    onBuy: (ServiceFeeResponse) -> Unit,
    onOpenPayment: (Long) -> Unit
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val parking = data.services.firstOrNull { it.category == "MOTO_PARKING" }
    val library = data.services.firstOrNull { it.category == "LIBRARY" }
    val profile = data.profile
    val year = profile.academicYear?.yearLabel ?: data.tuition.academicYear ?: "—"

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Rupp.colors.hero)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 18.dp)
        ) {
            HeaderBar(title = "RUPP Smart Campus Cards", subtitle = "DIGITAL CARDS", onBack = onBack)
            Spacer(Modifier.height(6.dp))
            Text(
                "Your student identity, motorbike parking pass and library card",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PillTabs(
                options = listOf(
                    Icons.Rounded.CreditCard to "Student ID",
                    Icons.Rounded.TwoWheeler to "Moto Park Card",
                    Icons.AutoMirrored.Rounded.MenuBook to "Library Pass"
                ),
                selected = tab,
                onSelect = { tab = it },
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )

            when (tab) {
                0 -> {
                    SmartCard(
                        title = "RUPP Student Identity Card",
                        holder = profile.fullName,
                        subtitle = "ID: ${profile.id} · Academic Year $year\n${profile.faculty?.facultyName ?: ""}",
                        footerLeft = cardNumber(2026, profile.id),
                        footerRight = "Valid thru: $year",
                        statusLabel = "Active",
                        statusActive = true,
                        skin = CardSkin.STUDENT
                    )
                    CardQr(
                        "RUPP-STUDENT|id=${profile.id}|user=${profile.user?.username}|year=$year",
                        "Show this code at campus check-points, exams and the Finance Office."
                    )
                    RuppCard {
                        SectionTitle("Card holder")
                        Spacer(Modifier.height(6.dp))
                        InfoRow("Name", profile.fullName)
                        InfoRow("Username", "@${profile.user?.username ?: "—"}")
                        InfoRow("Faculty", profile.faculty?.facultyName ?: "—")
                        InfoRow("Major", profile.major?.majorName ?: "—")
                        InfoRow("Academic year", year)
                    }
                }
                1 -> PassTab(parking, profile.fullName, profile.id, prefix = 3101, onBuy, onOpenPayment,
                    emptyText = "No moto parking pass is offered right now.")
                else -> PassTab(library, profile.fullName, profile.id, prefix = 4101, onBuy, onOpenPayment,
                    emptyText = "No library pass is offered right now.")
            }
        }
    }
}

@Composable
private fun PassTab(
    fee: ServiceFeeResponse?,
    holder: String,
    studentId: Long,
    prefix: Int,
    onBuy: (ServiceFeeResponse) -> Unit,
    onOpenPayment: (Long) -> Unit,
    emptyText: String
) {
    if (fee == null) {
        Text(emptyText, color = Rupp.colors.muted, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(24.dp))
        return
    }
    val visual = serviceVisual(fee.category)
    val active = fee.isPaid
    SmartCard(
        title = fee.name,
        holder = holder,
        subtitle = if (active) "Pass active · ${fee.validityLabel ?: "current period"}"
        else if (fee.inProgress) "Payment received · waiting for confirmation"
        else "Not active · ${Fmt.usd(fee.amount)} for ${fee.validityLabel ?: "this period"}",
        footerLeft = cardNumber(prefix, studentId),
        footerRight = "Valid thru: ${fee.validUntil?.let { Fmt.dateLong(it) } ?: "—"}",
        statusLabel = when {
            active -> "Active"
            fee.myStatus == "VERIFIED" -> "Confirming"
            fee.myStatus == "PENDING" -> "Pending"
            else -> "Inactive"
        },
        statusActive = active,
        skin = if (active || fee.inProgress) visual.skin else CardSkin.INACTIVE
    )

    when {
        active -> {
            CardQr(
                "RUPP-PASS|${fee.code}|student=$studentId|receipt=${fee.myReceiptNumber ?: "-"}",
                "Scan at the ${visual.label.lowercase()} gate. Valid until ${fee.validUntil?.let { Fmt.dateLong(it) } ?: "the end of the period"}."
            )
            RuppCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBadge(Icons.Rounded.Verified, Rupp.colors.success, Rupp.colors.successBg)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Paid ${Fmt.usd(fee.amount)}", style = MaterialTheme.typography.titleSmall)
                        Text(
                            listOfNotNull(fee.myReceiptNumber, fee.myPaidAt?.let { Fmt.dateLong(it) }).joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = Rupp.colors.muted
                        )
                    }
                }
                fee.myPaymentId?.let { id ->
                    Spacer(Modifier.height(10.dp))
                    SoftButton("View receipt", onClick = { onOpenPayment(id) })
                }
            }
        }
        fee.inProgress -> RuppCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(Icons.Rounded.HourglassTop, Rupp.colors.warning, Rupp.colors.warningBg)
                Spacer(Modifier.width(12.dp))
                Text(
                    if (fee.myStatus == "PENDING") "Finish verifying your payment with the one-time code to activate this pass."
                    else "Verified. The card turns on once the Finance Office confirms your payment.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            fee.myPaymentId?.let { id ->
                Spacer(Modifier.height(10.dp))
                GradientButton(if (fee.myStatus == "PENDING") "Verify payment" else "View payment", onClick = { onOpenPayment(id) })
            }
        }
        else -> RuppCard {
            SectionTitle(fee.name)
            Spacer(Modifier.height(6.dp))
            fee.description?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = Rupp.colors.muted) }
            Spacer(Modifier.height(6.dp))
            InfoRow("Price", Fmt.usd(fee.amount), bold = true)
            fee.validityLabel?.let { InfoRow("Covers", it) }
            Spacer(Modifier.height(10.dp))
            GradientButton("Get pass · ${Fmt.usd(fee.amount)}", onClick = { onBuy(fee) }, icon = visual.icon)
        }
    }
}

@Composable
private fun CardQr(payload: String, caption: String) {
    RuppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Rupp.colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(8.dp)
            ) { QrCode(payload, Modifier.fillMaxSize(), sizePx = 360) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Digital card code", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(caption, style = MaterialTheme.typography.bodySmall, color = Rupp.colors.muted)
            }
        }
    }
}
