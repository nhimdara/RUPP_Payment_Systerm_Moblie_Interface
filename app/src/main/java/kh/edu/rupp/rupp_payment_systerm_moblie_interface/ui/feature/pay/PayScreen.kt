package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.EmptyState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorBanner
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientHeader
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.HeaderBar
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.InfoRow
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PayMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SectionTitle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Skeleton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SoftButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusChip
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusTimeline
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.lifecycleSteps
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.paymentStatusStyle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.serviceVisual
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.tuitionStatusStyle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common.title
import kotlinx.coroutines.delay

/** Payments hub: tuition plus campus services, each paid through the checkout sheet. */
@Composable
fun PayScreen(
    vm: PayViewModel,
    onBackToHome: () -> Unit,
    onOpenPayment: (Long) -> Unit
) {
    val s by vm.state.collectAsState()

    Column(Modifier.fillMaxSize()) {
        GradientHeader {
            Spacer(Modifier.statusBarsPadding())
            HeaderBar(
                title = if (s.done) "Payment Submitted" else "Payments",
                subtitle = if (s.done) "RUPP SECURE PAYMENT" else "TUITION & CAMPUS SERVICES"
            )
        }
        when {
            s.done && s.payment != null -> DoneContent(s, onOpenPayment, onBackToHome = { vm.reset(); onBackToHome() })
            s.setup != null -> Hub(s, vm, onOpenPayment)
            s.loading -> PaySkeleton()
            else -> ErrorState(s.error ?: "Unknown error", onRetry = vm::load)
        }
    }

    if (s.target != null) CheckoutSheet(s, vm)
    OtpSheet(vm.otp, amountLabel = s.payment?.let { Fmt.usd(it.totalPaid) }, onVerified = vm::onVerified)
}

// ---------------------------------------------------------------------------
// Hub
// ---------------------------------------------------------------------------

@Composable
private fun Hub(s: PayUiState, vm: PayViewModel, onOpenPayment: (Long) -> Unit) {
    val setup = s.setup ?: return
    val t = setup.tuition
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tuition
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Rupp.colors.hero)
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(serviceVisual("TUITION").icon, Color.White, Color.White.copy(alpha = 0.16f))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("TUITION FEE · ${setup.fee.academicYear.yearLabel}", color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall)
                    Text(setup.fee.faculty.facultyName, color = Color.White, style = MaterialTheme.typography.titleSmall)
                }
                StatusChip(tuitionStatusStyle(t.paymentStatus))
            }
            Spacer(Modifier.height(14.dp))
            Text("Balance due", color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall)
            Text(Fmt.usd(t.remainingBalance), color = Color.White, style = MaterialTheme.typography.displaySmall)
            Text(
                "${Fmt.khr(t.remainingBalance)} · of ${Fmt.usd(t.totalTuitionFee)} · due ${Fmt.dateLong(setup.fee.dueDate)}",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(14.dp))
            if (t.fullyPaid || t.remainingBalance <= 0.0) {
                Text("✓ Fully paid for ${t.academicYear ?: "this year"}", color = RuppPalette.Green400, style = MaterialTheme.typography.titleSmall)
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(RuppPalette.Gold)
                        .clickable { vm.openTuition() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pay tuition", color = RuppPalette.Forest900, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        s.tuitionInProgress?.let { p ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Rupp.colors.warningBg)
                    .clickable { onOpenPayment(p.id) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.HourglassTop, null, tint = Rupp.colors.warning)
                Spacer(Modifier.width(10.dp))
                Text(
                    "A ${Fmt.usd(p.totalPaid)} tuition payment is ${paymentStatusStyle(p.status).label.lowercase()}. " +
                        "It counts once Finance confirms it. Tap to view.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Rupp.colors.warning
                )
            }
        }

        // Campus services
        SectionTitle("Campus Services")
        if (setup.services.isEmpty()) {
            EmptyState("No campus services yet", "Parking, library and other fees will appear here when the university adds them.")
        } else {
            RuppCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)) {
                setup.services.forEachIndexed { i, fee ->
                    ServiceRow(fee, onPay = { vm.openService(fee) }, onOpenPayment = onOpenPayment)
                    if (i < setup.services.lastIndex) {
                        Box(Modifier.fillMaxWidth().height(1.dp).background(Rupp.colors.cardBorder))
                    }
                }
            }
        }
        Text(
            "All payments are verified with a one-time code and confirmed by the RUPP Finance Office.",
            style = MaterialTheme.typography.bodySmall,
            color = Rupp.colors.muted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ServiceRow(fee: ServiceFeeResponse, onPay: () -> Unit, onOpenPayment: (Long) -> Unit) {
    val v = serviceVisual(fee.category)
    val c = Rupp.colors
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        IconBadge(v.icon, Color.White, v.color)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(fee.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                fee.validityLabel ?: fee.description ?: "One-time fee",
                style = MaterialTheme.typography.bodySmall,
                color = c.muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(8.dp))
        when {
            fee.isPaid -> Pill("Active", c.success, c.successBg) { fee.myPaymentId?.let(onOpenPayment) }
            fee.inProgress -> Pill(if (fee.myStatus == "PENDING") "Verify" else "Confirming", c.warning, c.warningBg) {
                fee.myPaymentId?.let(onOpenPayment)
            }
            else -> Pill("Pay ${Fmt.usd(fee.amount)}", Color.White, MaterialTheme.colorScheme.primary, onPay)
        }
    }
}

@Composable
private fun Pill(text: String, fg: Color, bg: Color, onClick: () -> Unit) {
    Text(
        text,
        color = fg,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

// ---------------------------------------------------------------------------
// Checkout sheet (Top Up style): amount, method tiles, KHQR, pay button
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutSheet(s: PayUiState, vm: PayViewModel) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val clipboard = LocalClipboardManager.current
    val setup = s.setup
    val target = s.target ?: return
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(s.qrDeadlineMillis) { while (true) { now = System.currentTimeMillis(); delay(1000) } }
    val secondsLeft = (s.qrDeadlineMillis - now) / 1000
    val studentName = setup?.tuition?.studentName ?: "Student"

    ModalBottomSheet(onDismissRequest = vm::closeCheckout, sheetState = sheet, containerColor = MaterialTheme.colorScheme.background) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header
            Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(CircleShape).background(Color.White).padding(3.dp)) {
                    Image(painterResource(R.drawable.rupp_logo), contentDescription = null)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(s.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        when (target) {
                            is PayTarget.Tuition -> "Balance due: ${Fmt.usd(s.tuitionRemaining)}"
                            is PayTarget.Service -> target.fee.validityLabel ?: target.fee.description ?: "Campus service"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Rupp.colors.muted
                    )
                }
                IconButton(onClick = vm::closeCheckout, modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)) {
                    Icon(Icons.Rounded.Close, "Close")
                }
            }
            Spacer(Modifier.height(14.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(Rupp.colors.cardBorder))

            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Amount
                Label(if (target is PayTarget.Tuition) "SELECT AMOUNT (USD)" else "AMOUNT (USD)")
                if (target is PayTarget.Tuition) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AmountChip("Full balance", Fmt.usd(s.tuitionRemaining), s.amountOption == AmountOption.FULL, Modifier.weight(1f)) {
                            vm.selectAmount(AmountOption.FULL)
                        }
                        AmountChip("Half (50%)", Fmt.usd(s.tuitionRemaining / 2), s.amountOption == AmountOption.HALF, Modifier.weight(1f)) {
                            vm.selectAmount(AmountOption.HALF)
                        }
                    }
                    OutlinedTextField(
                        value = s.customAmount,
                        onValueChange = vm::onCustomAmount,
                        placeholder = { Text("Or enter custom amount") },
                        prefix = { Text("$ ") },
                        singleLine = true,
                        isError = s.amountError != null,
                        supportingText = if (s.amountError != null) {
                            { Text(s.amountError.orEmpty()) }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    val fee = (target as PayTarget.Service).fee
                    val v = serviceVisual(fee.category)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, Rupp.colors.cardBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconBadge(v.icon, Color.White, v.color)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(v.label, style = MaterialTheme.typography.titleSmall)
                            Text(fee.description ?: "Campus service fee", style = MaterialTheme.typography.bodySmall, color = Rupp.colors.muted, maxLines = 2)
                        }
                        Text(Fmt.usd(fee.amount), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    }
                }

                // Method tiles
                Label("PAYMENT METHOD")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf(PayMethod.KHQR_BAKONG, PayMethod.ABA_PAYWAY, PayMethod.WING).forEach { m ->
                        MethodTile(m, selected = s.method == m, modifier = Modifier.weight(1f)) { vm.selectMethod(m) }
                    }
                }

                // KHQR panel
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, Rupp.colors.cardBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.QrCode2, null, tint = methodBrand(s.method).first, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (s.method == PayMethod.KHQR_BAKONG) "KHQR Individual" else "${s.method.title} QR",
                            color = methodBrand(s.method).first,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text("RUPP Finance Acct", style = MaterialTheme.typography.labelMedium, color = Rupp.colors.muted)
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Rupp.colors.cardBorder))
                    Spacer(Modifier.height(14.dp))
                    val payload = "RUPP-PAY|${if (target is PayTarget.Service) "service=${target.fee.code}" else "fee=${setup?.fee?.id}"}" +
                        "|student=${setup?.tuition?.studentId}|amount=${"%.2f".format(s.amount)}|currency=USD|ref=${s.reference}|method=${s.method.code}"
                    Box(
                        Modifier
                            .size(196.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White)
                            .border(1.dp, Rupp.colors.cardBorder, RoundedCornerShape(18.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        QrCode(payload, Modifier.fillMaxSize())
                        val (brand, short) = methodBrand(s.method)
                        Text(
                            short,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(brand)
                                .border(2.dp, Color.White, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Scan with Bakong or any Cambodian banking app", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                    Text(
                        "Acct name: RUPP TREASURY (${studentName.uppercase()})",
                        style = MaterialTheme.typography.labelMedium,
                        color = Rupp.colors.muted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Amount: ${Fmt.usd(s.amount)} USD (${Fmt.khr(s.amount)})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { clipboard.setText(AnnotatedString(s.reference)) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Reference (add to transfer note)", style = MaterialTheme.typography.labelSmall, color = Rupp.colors.muted)
                            Text(s.reference, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.titleSmall)
                        }
                        Icon(Icons.Rounded.ContentCopy, "Copy reference", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Timer, null, tint = if (secondsLeft < 60) Rupp.colors.danger else Rupp.colors.muted, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            if (secondsLeft > 0) "QR expires in ${Fmt.mmss(secondsLeft)}" else "QR expired. Close and reopen to refresh.",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (secondsLeft < 60) Rupp.colors.danger else Rupp.colors.muted
                        )
                    }
                }

                s.submitError?.let { ErrorBanner(it) }
            }

            Box(Modifier.fillMaxWidth().height(1.dp).background(Rupp.colors.cardBorder))
            Box(Modifier.padding(20.dp)) {
                GradientButton(
                    text = "I've paid ${Fmt.usd(s.amount)}",
                    icon = Icons.AutoMirrored.Rounded.ArrowForward,
                    loading = s.submitting,
                    enabled = s.canPay && secondsLeft > 0,
                    onClick = vm::submit
                )
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text, style = MaterialTheme.typography.labelMedium, color = Rupp.colors.muted, fontWeight = FontWeight.Bold)
}

/** Brand colour and short mark for each payment channel tile. */
private fun methodBrand(m: PayMethod): Pair<Color, String> = when (m) {
    PayMethod.KHQR_BAKONG -> Color(0xFFD62828) to "KHQR"
    PayMethod.ABA_PAYWAY -> Color(0xFF0B5E86) to "ABA"
    PayMethod.WING -> Color(0xFF7AB929) to "WING"
    PayMethod.ACLEDA -> Color(0xFF1D3F8F) to "ACLEDA"
}

@Composable
private fun MethodTile(m: PayMethod, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val (brand, short) = methodBrand(m)
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) brand.copy(alpha = 0.06f) else MaterialTheme.colorScheme.surface)
            .border(if (selected) 2.dp else 1.dp, if (selected) brand else Rupp.colors.cardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(width = 44.dp, height = 30.dp).clip(RoundedCornerShape(8.dp)).background(brand),
            contentAlignment = Alignment.Center
        ) { Text(short, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold) }
        Spacer(Modifier.height(6.dp))
        Text(
            when (m) { PayMethod.KHQR_BAKONG -> "Bakong"; PayMethod.ABA_PAYWAY -> "ABA Pay"; PayMethod.WING -> "Wing Bank"; else -> m.title },
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
        Text(
            when (m) { PayMethod.KHQR_BAKONG -> "Any Khmer bank"; PayMethod.ABA_PAYWAY -> "Instant push"; PayMethod.WING -> "Wallet / App"; else -> "" },
            style = MaterialTheme.typography.labelSmall,
            color = Rupp.colors.muted,
            maxLines = 1
        )
    }
}

@Composable
private fun AmountChip(title: String, value: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .border(1.dp, if (selected) Color.Transparent else Rupp.colors.cardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = if (selected) Color.White.copy(alpha = 0.8f) else Rupp.colors.muted)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface)
    }
}

// ---------------------------------------------------------------------------
// Success
// ---------------------------------------------------------------------------

@Composable
private fun DoneContent(s: PayUiState, onOpenPayment: (Long) -> Unit, onBackToHome: () -> Unit) {
    val p = s.payment ?: return
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AnimatedVisibility(visible = appeared, enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()) {
            Box(Modifier.size(96.dp).clip(CircleShape).background(Rupp.colors.successBg), contentAlignment = Alignment.Center) {
                Box(Modifier.size(70.dp).clip(CircleShape).background(Rupp.colors.success), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Check, null, tint = Color.White, modifier = Modifier.size(42.dp))
                }
            }
        }
        Text("Payment Verified!", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Your ${Fmt.usd(p.totalPaid)} payment for ${p.title()} was verified. The RUPP Finance Office will confirm it and issue your official receipt.",
            style = MaterialTheme.typography.bodyMedium,
            color = Rupp.colors.muted,
            textAlign = TextAlign.Center
        )
        StatusChip(paymentStatusStyle(p.status))
        RuppCard(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Rupp.colors.successBg).padding(14.dp)
            ) {
                Column {
                    Text("TOTAL AMOUNT PAID", style = MaterialTheme.typography.labelSmall, color = Rupp.colors.success)
                    Text(Fmt.usd(p.totalPaid), style = MaterialTheme.typography.headlineMedium, color = Rupp.colors.success)
                    Text(Fmt.khr(p.totalPaid), style = MaterialTheme.typography.bodySmall, color = Rupp.colors.success)
                }
            }
            Spacer(Modifier.height(8.dp))
            InfoRow("For", p.title())
            InfoRow("Student", p.studentName)
            InfoRow("Method", PayMethod.label(p.details.firstOrNull()?.method))
            InfoRow("Reference", p.details.firstOrNull()?.transactionRef ?: "—")
            InfoRow("Payment ID", "#${p.id}")
            InfoRow("Date & time", Fmt.dateTime(p.paymentDate))
        }
        RuppCard(Modifier.fillMaxWidth()) {
            SectionTitle("What happens next")
            Spacer(Modifier.height(10.dp))
            StatusTimeline(lifecycleSteps(p.status, p.paymentDate, p.receipt?.receiptNumber))
        }
        GradientButton("View payment details", onClick = { onOpenPayment(p.id) }, icon = Icons.Rounded.ReceiptLong)
        SoftButton("Back to Dashboard", onClick = onBackToHome, icon = Icons.Rounded.Home)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PaySkeleton() {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Skeleton(Modifier.fillMaxWidth().height(200.dp), radius = 22.dp)
        Skeleton(Modifier.fillMaxWidth().height(60.dp), radius = 16.dp)
        Skeleton(Modifier.fillMaxWidth().height(240.dp), radius = 20.dp)
    }
}
