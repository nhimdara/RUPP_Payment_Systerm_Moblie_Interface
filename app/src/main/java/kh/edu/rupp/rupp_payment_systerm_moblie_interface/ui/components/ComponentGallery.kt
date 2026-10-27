package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

// Preview-only page that shows every shared component, so the team can check them in light and dark
@Composable
private fun ComponentGallery() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Components", subtitle = "Shared UI kit", onBack = {})
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard(title = "Status badges") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentStatus.entries.forEach { PaymentStatusBadge(it) }
                    listOf("PAID", "PARTIAL", "UNPAID").forEach { TuitionStatusBadge(it) }
                    ActiveBadge()
                }
            }
            SectionCard(title = "Info rows") {
                InfoRow(label = "Tuition fee", value = formatUsd(600.0))
                InfoRow(label = "Outstanding balance", value = formatUsd(300.0), emphasize = true)
                BalanceProgress(paid = 300.0, total = 600.0, modifier = Modifier.padding(top = 8.dp))
            }
            SectionCard(title = "States") {
                EmptyState(title = "No payments yet", message = "Nothing to show here.")
                ErrorState(message = "An error occurred: timeout", onRetry = {})
            }
            PrimaryButton(text = "Pay $300.00", onClick = {})
            PrimaryButton(text = "Pay $300.00", onClick = {}, loading = true)
        }
    }
}

@Preview(name = "Components - light", showBackground = true, heightDp = 1400)
@Preview(
    name = "Components - dark",
    showBackground = true,
    heightDp = 1400,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ComponentGalleryPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme {
        ComponentGallery()
    }
}
