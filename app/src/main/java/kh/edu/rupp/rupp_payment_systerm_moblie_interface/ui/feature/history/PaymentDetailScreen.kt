package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentStatus
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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SectionTitle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Skeleton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.SoftButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusChip
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.StatusTimeline
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.lifecycleSteps
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.paymentStatusStyle
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common.title
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.OtpSheet
import java.io.File

@Composable
fun PaymentDetailScreen(vm: PaymentDetailViewModel, onBack: () -> Unit, openReceipt: Boolean = false) {
    val s by vm.state.collectAsState()
    val context = LocalContext.current
    var showReceipt by rememberSaveable { mutableStateOf(openReceipt) }

    LaunchedEffect(s.message) {
        s.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show(); vm.clearMessage() }
    }

    val shownPayment = s.payment
    val shownReceipt = s.receipt
    if (showReceipt && shownPayment != null && shownReceipt != null) {
        BackHandler { if (openReceipt) onBack() else showReceipt = false }
        OfficialReceiptScreen(
            payment = shownPayment,
            receipt = shownReceipt,
            downloading = s.downloading,
            onClose = { if (openReceipt) onBack() else showReceipt = false },
            onSavePdf = { vm.downloadReceipt(context) { file -> openPdf(context, file) } },
            onShare = { vm.downloadReceipt(context) { file -> sharePdf(context, file) } }
        )
        return
    }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GradientHeader {
            Spacer(Modifier.statusBarsPadding())
            HeaderBar(
                title = s.payment?.let { "Payment #${it.id}" } ?: "Payment",
                subtitle = "TRANSACTION DETAILS",
                onBack = onBack
            )
        }
        val p = s.payment
        when {
            p != null -> Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val style = paymentStatusStyle(p.status)
                RuppCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(p.title(), style = MaterialTheme.typography.titleSmall, color = Rupp.colors.muted)
                            Text(Fmt.usd(p.totalPaid), style = MaterialTheme.typography.displaySmall)
                            Text(Fmt.khr(p.totalPaid), style = MaterialTheme.typography.bodySmall, color = Rupp.colors.muted)
                        }
                        StatusChip(style)
                    }
                    if (p.status == PaymentStatus.PENDING) {
                        Spacer(Modifier.height(12.dp))
                        GradientButton("Verify with one-time code", onClick = vm::startVerification, icon = Icons.Rounded.Lock)
                    }
                }

                RuppCard {
                    SectionTitle("Status")
                    Spacer(Modifier.height(10.dp))
                    StatusTimeline(lifecycleSteps(p.status, p.paymentDate, s.receipt?.receiptNumber))
                }

                RuppCard {
                    SectionTitle("Details")
                    Spacer(Modifier.height(6.dp))
                    InfoRow("Student", p.studentName)
                    InfoRow("Email", p.studentEmail)
                    InfoRow("Faculty", p.facultyName ?: "—")
                    InfoRow("Academic year", p.academicYearLabel ?: "—")
                    if (p.isService) {
                        InfoRow("Campus service", p.serviceFeeName ?: "—")
                    } else {
                        InfoRow("Fee structure", "#${p.feeStructureId ?: "—"} · ${Fmt.usd(p.feeStructureAmount)}")
                    }
                    InfoRow("Submitted", Fmt.dateTime(p.paymentDate))
                    p.remarks?.takeIf { it.isNotBlank() }?.let { InfoRow("Remarks", it) }
                    p.details.forEach { d ->
                        InfoRow("Method", PayMethod.label(d.method))
                        InfoRow("Reference", d.transactionRef)
                    }
                }

                val receipt = s.receipt
                if (receipt != null) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Rupp.colors.hero)
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconBadge(Icons.Rounded.Verified, Color.White, Color.White.copy(alpha = 0.16f))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("OFFICIAL ELECTRONIC RECEIPT", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                                Text(receipt.receiptNumber, color = Color.White, style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Monospace)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text("Issued ${Fmt.dateTime(receipt.issueDate)} · ${Fmt.usd(receipt.totalAmount)}", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                    }
                    GradientButton(
                        "View Official Receipt",
                        icon = Icons.Rounded.Verified,
                        onClick = { showReceipt = true }
                    )
                    SoftButton(
                        "Open PDF receipt",
                        icon = Icons.Rounded.Download,
                        enabled = !s.downloading,
                        onClick = { vm.downloadReceipt(context) { file -> openPdf(context, file) } }
                    )
                    SoftButton(
                        "Share receipt",
                        icon = Icons.Rounded.Share,
                        enabled = !s.downloading,
                        onClick = { vm.downloadReceipt(context) { file -> sharePdf(context, file) } }
                    )
                } else if (p.status == PaymentStatus.PENDING || p.status == PaymentStatus.VERIFIED) {
                    RuppCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.ReceiptLong, null, tint = Rupp.colors.muted)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Your official PDF receipt will be available here once the Finance Office confirms this payment.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Rupp.colors.muted
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            s.loading -> Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Skeleton(Modifier.fillMaxWidth().height(120.dp), radius = 20.dp)
                Skeleton(Modifier.fillMaxWidth().height(200.dp), radius = 20.dp)
                Skeleton(Modifier.fillMaxWidth().height(220.dp), radius = 20.dp)
            }
            else -> ErrorState(s.error ?: "Unknown error", onRetry = vm::load)
        }
    }

    OtpSheet(vm.otp, amountLabel = s.payment?.let { Fmt.usd(it.totalPaid) }, onVerified = { vm.onVerified() })
}

private fun uriFor(context: Context, file: File) =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

private fun openPdf(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_VIEW)
        .setDataAndType(uriFor(context, file), "application/pdf")
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        context.startActivity(Intent.createChooser(intent, "Open receipt"))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No PDF viewer found. Try Share instead.", Toast.LENGTH_LONG).show()
    }
}

private fun sharePdf(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_SEND)
        .setType("application/pdf")
        .putExtra(Intent.EXTRA_STREAM, uriFor(context, file))
        .putExtra(Intent.EXTRA_SUBJECT, "RUPP tuition receipt ${file.nameWithoutExtension.removePrefix("receipt-")}")
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(intent, "Share receipt"))
}
