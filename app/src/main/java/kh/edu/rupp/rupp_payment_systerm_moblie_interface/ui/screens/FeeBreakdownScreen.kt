package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.TuitionCalculationResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.sample.SampleData
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.AppHeader
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.BalanceProgress
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.EmptyState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.InfoRow
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentStatusBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PrimaryButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.SectionCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.TuitionStatusBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.UiStateContent
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatApiDate
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatApiDateTime
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatPaymentMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.formatUsd
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.isDarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.isPastDue
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.CardGradientDark
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.CardGradientLight
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

// Fee breakdown and outstanding balance. Data: GET /api/v1/tuition-fees/calculate
@Composable
fun FeeBreakdownScreen(
    state: UiState<TuitionCalculationResponse>,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onPayNow: () -> Unit = {},
    onPaymentClick: (PaymentResponse) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppHeader(title = "Fee Breakdown", subtitle = "Outstanding balance", onBack = onBack)
        UiStateContent(state = state, modifier = Modifier.fillMaxSize(), onRetry = onRetry) { calculation ->
            FeeBreakdownContent(calculation, onPayNow, onPaymentClick)
        }
    }
}

@Composable
private fun FeeBreakdownContent(
    calculation: TuitionCalculationResponse,
    onPayNow: () -> Unit,
    onPaymentClick: (PaymentResponse) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BalanceCard(calculation) }
            item { BreakdownSection(calculation) }
            item { StudentSection(calculation) }
            item { HistorySection(calculation.paymentHistory, onPaymentClick) }
        }
        // Hide "Pay Now" when fullyPaid is true (API doc section 10)
        if (!calculation.fullyPaid) {
            Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
                PrimaryButton(
                    text = "Pay ${formatUsd(calculation.remainingBalance)}",
                    onClick = onPayNow,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(calculation: TuitionCalculationResponse) {
    val overdue = !calculation.fullyPaid && isPastDue(calculation.dueDate)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(if (isDarkBackground()) CardGradientDark else CardGradientLight)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (calculation.fullyPaid) "All fees paid" else "Outstanding balance",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.weight(1f)
            )
            TuitionStatusBadge(calculation.paymentStatus, onDarkBackground = true)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatUsd(calculation.remainingBalance),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = when {
                calculation.fullyPaid -> "Academic year ${calculation.academicYear ?: "-"}"
                overdue -> "Overdue since ${formatApiDate(calculation.dueDate)}"
                else -> "Due ${formatApiDate(calculation.dueDate)}"
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (overdue) FontWeight.SemiBold else FontWeight.Normal,
            color = if (overdue) Color(0xFFFFCDD2) else Color.White.copy(alpha = 0.85f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        BalanceProgress(
            paid = calculation.totalPaid,
            total = calculation.totalTuitionFee,
            progressColor = Color.White,
            trackColor = Color.White.copy(alpha = 0.25f),
            labelColor = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun BreakdownSection(calculation: TuitionCalculationResponse) {
    val paidColor = MaterialTheme.colorScheme.primary
    SectionCard(title = "Fee breakdown") {
        InfoRow(
            label = "Tuition fee ${calculation.academicYear ?: ""}".trim(),
            value = formatUsd(calculation.totalTuitionFee)
        )
        InfoRow(
            label = "Amount paid",
            value = "- ${formatUsd(calculation.totalPaid)}",
            valueColor = paidColor
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
        InfoRow(
            label = "Outstanding balance",
            value = formatUsd(calculation.remainingBalance),
            valueColor = if (calculation.remainingBalance > 0) MaterialTheme.colorScheme.error else paidColor,
            emphasize = true
        )
        InfoRow(label = "Due date", value = formatApiDate(calculation.dueDate))
    }
}

@Composable
private fun StudentSection(calculation: TuitionCalculationResponse) {
    SectionCard(title = "Student") {
        InfoRow(label = "Name", value = calculation.studentName)
        InfoRow(label = "Faculty", value = calculation.facultyName ?: "-")
        InfoRow(label = "Major", value = calculation.majorName ?: "-")
        InfoRow(label = "Academic year", value = calculation.academicYear ?: "-")
    }
}

@Composable
private fun HistorySection(
    payments: List<PaymentResponse>,
    onPaymentClick: (PaymentResponse) -> Unit
) {
    SectionCard(title = "Payments for this fee") {
        if (payments.isEmpty()) {
            EmptyState(
                title = "No payments yet",
                message = "Payments you make for this fee will show up here.",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            payments.forEachIndexed { index, payment ->
                if (index > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                }
                PaymentRow(payment, onClick = { onPaymentClick(payment) })
            }
        }
    }
}

@Composable
private fun PaymentRow(
    payment: PaymentResponse,
    onClick: () -> Unit
) {
    val method = payment.details
        .map { formatPaymentMethod(it.method) }
        .distinct()
        .joinToString(", ")
        .ifEmpty { "Payment #${payment.id}" }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = method,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatApiDateTime(payment.paymentDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatUsd(payment.totalPaid),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(4.dp))
            PaymentStatusBadge(payment.status)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeeBreakdownPartialPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        FeeBreakdownScreen(state = UiState.Success(SampleData.partiallyPaid), onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FeeBreakdownOverduePreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        FeeBreakdownScreen(state = UiState.Success(SampleData.unpaid), onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FeeBreakdownPaidPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        FeeBreakdownScreen(state = UiState.Success(SampleData.fullyPaid), onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FeeBreakdownErrorPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        FeeBreakdownScreen(state = UiState.Error("An error occurred: connection refused"), onBack = {})
    }
}
