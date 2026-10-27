package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentHistoryItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentHistoryCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentHistoryFilterSection
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentHistorySummaryCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentHistoryTopBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentReceiptBottomSheet
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.RuppBottomNavBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.RuppNavTab
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppCardBorder
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppDarkBg
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppLightGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppPrimaryGreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextMuted
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextPrimary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RuppTextSecondary
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.viewmodel.PaymentHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PaymentHistoryScreen(
    viewModel: PaymentHistoryViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onTabSelected: (RuppNavTab) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(

        containerColor = RuppDarkBg,
        topBar = {
            PaymentHistoryTopBar(
                title = "Payment History",
                subtitle = "Sokha Chan • ID 20260142",
                isSearchActive = uiState.isSearchActive,
                onBackClick = onNavigateBack,
                onSearchToggle = { viewModel.toggleSearch() },
                onFilterClick = {}
            )
        },
        bottomBar = {
            RuppBottomNavBar(
                selectedTab = RuppNavTab.ANALYTIC,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    PaymentHistorySummaryCard(
                        summary = uiState.summary,
                        onDownloadStatementClick = {
                            Toast.makeText(
                                context,
                                "Generating Official RUPP Statement...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }

            // Filters & Search
            item {
                PaymentHistoryFilterSection(
                    selectedCategory = uiState.selectedCategory,
                    selectedStatus = uiState.selectedStatus,
                    searchQuery = uiState.searchQuery,
                    isSearchActive = uiState.isSearchActive,
                    onCategorySelected = { viewModel.onCategorySelected(it) },
                    onStatusSelected = { viewModel.onStatusSelected(it) },
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                )
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transactions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuppTextPrimary
                    )

                    Text(
                        text = "${uiState.transactions.size} records found",
                        fontSize = 12.sp,
                        color = RuppTextSecondary
                    )
                }
            }

            // Empty state if no results match
            if (uiState.transactions.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(RuppPrimaryGreen.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = "No Results",
                                tint = RuppTextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No Transactions Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = RuppTextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (uiState.searchQuery.isNotEmpty())
                                "No payment history matching \"${uiState.searchQuery}\""
                            else
                                "No transactions recorded for the selected filter.",
                            fontSize = 13.sp,
                            color = RuppTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                // Grouped transaction lists
                uiState.groupedTransactions.forEach { (monthGroup, items) ->
                    item(key = monthGroup) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = monthGroup,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RuppLightGreen
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(RuppCardBorder)
                            )
                        }
                    }

                    items(
                        items = items,
                        key = { it.id }
                    ) { transaction ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            PaymentHistoryCard(
                                item = transaction,
                                onClick = {
                                    viewModel.onTransactionClicked(transaction)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Receipt Bottom Sheet
        if (uiState.selectedTransactionForDetail != null) {
            PaymentReceiptBottomSheet(
                item = uiState.selectedTransactionForDetail,
                sheetState = sheetState,
                onDismiss = { viewModel.onDismissDetail() },
                onDownloadPdf = {
                    Toast.makeText(
                        context,
                        "Downloading receipt ${uiState.selectedTransactionForDetail?.invoiceNo}.pdf",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.onDismissDetail()
                },
                onShareReceipt = {
                    Toast.makeText(
                        context,
                        "Sharing receipt ${uiState.selectedTransactionForDetail?.invoiceNo}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1411)
@Composable
fun PaymentHistoryScreenPreview() {
    RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
        PaymentHistoryScreen()
    }
}
