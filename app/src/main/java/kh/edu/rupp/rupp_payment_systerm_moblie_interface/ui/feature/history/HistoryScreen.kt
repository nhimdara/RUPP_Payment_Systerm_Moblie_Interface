package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.EmptyState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientHeader
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.HeaderBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Skeleton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common.PaymentListItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common.title
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(vm: HistoryViewModel, onOpenPayment: (Long) -> Unit, onOpenReceipt: (Long) -> Unit) {
    val s by vm.state.collectAsState()
    var showReceipts by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Infinite scroll: fetch the next page when the last row comes into view.
    val nearEnd by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 3
        }
    }
    LaunchedEffect(nearEnd, s.items.size) { if (nearEnd) vm.loadMore() }

    Column(Modifier.fillMaxSize()) {
        GradientHeader {
            Spacer(Modifier.statusBarsPadding())
            HeaderBar(title = "Payment History", subtitle = "TUITION & CAMPUS SERVICES") {
                IconButton(
                    onClick = { showReceipts = true },
                    modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.14f))
                ) {
                    Icon(Icons.Rounded.ReceiptLong, contentDescription = "Official receipts", tint = Color.White)
                }
            }
            Spacer(Modifier.height(14.dp))
            SearchField(s.query, vm::setQuery)
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(HistoryFilter.entries) { f ->
                    val selected = s.filter == f
                    Text(
                        "${f.label} (${s.count(f)})",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) RuppPalette.Forest900 else Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) RuppPalette.Gold else Color.White.copy(alpha = 0.14f))
                            .clickable { vm.setFilter(f) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        PullToRefreshBox(
            isRefreshing = s.refreshing,
            onRefresh = { vm.refresh() },
            modifier = Modifier.weight(1f)
        ) {
            when {
                s.loading -> Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Skeleton(Modifier.fillMaxWidth().height(110.dp), radius = 20.dp)
                    repeat(4) { Skeleton(Modifier.fillMaxWidth().height(70.dp), radius = 16.dp) }
                }
                s.items.isEmpty() && s.error != null -> ErrorState(s.error ?: "", onRetry = { vm.refresh(initial = true) })
                else -> LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item { SummaryCard(s) }
                    val visible = s.visible
                    if (visible.isEmpty()) {
                        item {
                            EmptyState(
                                if (s.items.isEmpty()) "No payments yet" else "No matches",
                                if (s.items.isEmpty()) "When you pay tuition, every transaction shows up here."
                                else "Try another filter or search term.",
                                icon = Icons.Rounded.ReceiptLong
                            )
                        }
                    }
                    // Group by month, newest first (the API already sorts by paymentDate desc).
                    visible.groupBy { Fmt.monthGroup(it.paymentDate) }.forEach { (month, payments) ->
                        item(key = "h-$month") {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                Text(month, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                                Text(
                                    "${payments.size} transaction${if (payments.size == 1) "" else "s"}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Rupp.colors.muted
                                )
                            }
                        }
                        item(key = "g-$month") {
                            RuppCard(contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)) {
                                payments.forEach { p -> PaymentListItem(p, onClick = { onOpenPayment(p.id) }) }
                            }
                        }
                    }
                    if (s.loadingMore) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                            }
                        }
                    } else if (s.lastPage && s.items.isNotEmpty()) {
                        item {
                            Text(
                                "Synced with RUPP Finance · ${s.totalElements} payment${if (s.totalElements == 1L) "" else "s"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Rupp.colors.muted,
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (showReceipts) {
        ReceiptsSheet(s.items, onDismiss = { showReceipts = false }, onOpen = { showReceipts = false; onOpenReceipt(it) })
    }
}

/** "RUPP Official Receipts": every confirmed payment that has an e-receipt. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReceiptsSheet(items: List<PaymentResponse>, onDismiss: () -> Unit, onOpen: (Long) -> Unit) {
    val receipts = items.filter { it.receipt != null }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
        Column(Modifier.padding(horizontal = 20.dp).navigationBarsPadding()) {
            Text("RUPP Official Receipts", style = MaterialTheme.typography.titleLarge)
            Text(
                "${receipts.size} verified electronic document${if (receipts.size == 1) "" else "s"}",
                style = MaterialTheme.typography.bodySmall,
                color = Rupp.colors.muted
            )
            Spacer(Modifier.height(14.dp))
            if (receipts.isEmpty()) {
                EmptyState("No receipts yet", "Receipts appear after the Finance Office confirms a payment.", icon = Icons.Rounded.ReceiptLong)
            }
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                receipts.forEach { p ->
                    val r = p.receipt!!
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, Rupp.colors.cardBorder, RoundedCornerShape(18.dp))
                            .clickable { onOpen(p.id) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconBadge(Icons.Rounded.Description, Rupp.colors.success, Rupp.colors.successBg)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.title(), style = MaterialTheme.typography.titleSmall, maxLines = 2)
                            Text(
                                "${r.receiptNumber} · ${Fmt.dateTime(r.issueDate)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = Rupp.colors.muted
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(Fmt.usd(r.totalAmount), style = MaterialTheme.typography.titleSmall)
                            Text("Verified", style = MaterialTheme.typography.labelSmall, color = Rupp.colors.success)
                        }
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = Rupp.colors.muted)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SearchField(value: String, onChange: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Search, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text("Search reference, receipt no., method…", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodyMedium)
            }
            BasicTextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                cursorBrush = SolidColor(RuppPalette.Gold),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (value.isNotEmpty()) {
            Icon(
                Icons.Rounded.Close, "Clear search", tint = Color.White,
                modifier = Modifier.size(20.dp).clickable { onChange("") }
            )
        }
    }
}

@Composable
private fun SummaryCard(s: HistoryUiState) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Rupp.colors.hero)
            .padding(18.dp)
    ) {
        Text("VERIFIED SETTLED", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
        Text(Fmt.usd(s.confirmedTotal), color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Text(Fmt.khr(s.confirmedTotal), color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("${s.count(HistoryFilter.CONFIRMED)} confirmed", RuppPalette.Green400)
            if (s.inProgressTotal > 0) Pill("${Fmt.usd(s.inProgressTotal)} in progress", RuppPalette.Gold)
            Pill("${s.totalElements} total", Color.White)
        }
    }
}

@Composable
private fun Pill(text: String, color: Color) {
    Text(
        text,
        color = color,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}
