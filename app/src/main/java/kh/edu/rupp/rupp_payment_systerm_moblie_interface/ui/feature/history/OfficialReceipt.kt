package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.ReceiptResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Fmt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.PayMethod
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.common.title
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.QrCode

private val ReceiptInk = Color(0xFF1B2A22)
private val ReceiptMuted = Color(0xFF6B756F)
private val StampRed = Color(0xFFC62828)

/**
 * Official electronic receipt (the RUPP layout: Kingdom of Cambodia motto, emblem, bilingual
 * university name, PAID stamp and verification QR) for a CONFIRMED payment.
 * The paper itself is always light so it looks like the printed receipt in dark mode too.
 */
@Composable
fun OfficialReceiptScreen(
    payment: PaymentResponse,
    receipt: ReceiptResponse,
    downloading: Boolean,
    onClose: () -> Unit,
    onSavePdf: () -> Unit,
    onShare: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Top bar
        Row(
            Modifier
                .fillMaxWidth()
                .background(Rupp.colors.hero)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Verified, null, tint = RuppPalette.Green400, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Official Electronic Receipt", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(receipt.receiptNumber, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f), fontFamily = FontFamily.Monospace)
            }
            IconButton(onClick = onClose, modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.16f))) {
                Icon(Icons.Rounded.Close, "Close", tint = Color.White)
            }
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ReceiptPaper(payment, receipt)
        }

        // Bottom actions
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(onClick = onSavePdf, enabled = !downloading, shape = RoundedCornerShape(14.dp), modifier = Modifier.height(48.dp)) {
                if (downloading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                else Icon(Icons.Rounded.Print, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Print / Save PDF")
            }
            OutlinedButton(onClick = onShare, enabled = !downloading, shape = RoundedCornerShape(14.dp), modifier = Modifier.height(48.dp)) {
                Icon(Icons.Rounded.Share, "Share receipt", modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.weight(1f))
            androidx.compose.material3.Button(
                onClick = onClose,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ReceiptPaper(p: PaymentResponse, r: ReceiptResponse) {
    val detail = p.details.firstOrNull()
    val shape = RoundedCornerShape(22.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFFFFFFFD))
            .border(1.dp, Color(0xFFE4E8E2), shape)
    ) {
        // Big faded emblem behind the paper, like a watermark
        Image(
            painterResource(R.drawable.rupp_logo),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center).size(230.dp).alpha(0.05f)
        )
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("KINGDOM OF CAMBODIA · NATION RELIGION KING", color = ReceiptMuted, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
            Text("ព្រះរាជាណាចក្រកម្ពុជា ជាតិ សាសនា ព្រះមហាក្សត្រ", color = RuppPalette.Forest700, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Image(painterResource(R.drawable.rupp_logo), contentDescription = "RUPP emblem", modifier = Modifier.size(52.dp))
            Spacer(Modifier.height(8.dp))
            Text("ROYAL UNIVERSITY OF PHNOM PENH", color = RuppPalette.Forest800, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Text("សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ (RUPP)", color = ReceiptInk, fontSize = 12.sp)
            Text("Finance, Accounting & Student Payment Department", color = ReceiptMuted, fontSize = 11.sp)

            Dashed()
            Row(Modifier.fillMaxWidth()) {
                Field("RECEIPT NUMBER", r.receiptNumber, Modifier.weight(1f), mono = true)
                Field("PAYMENT DATE", Fmt.dateTime(p.paymentDate), Modifier.weight(1f), end = true)
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                Field("STUDENT NAME", p.studentName, Modifier.weight(1f))
                Field("STUDENT ID", p.studentId.toString(), Modifier.weight(1f), end = true)
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                Field("FACULTY / DEPARTMENT", p.facultyName ?: "—", Modifier.weight(1f))
                Field("ACADEMIC YEAR", p.academicYearLabel ?: "—", Modifier.weight(1f), end = true)
            }

            Dashed()
            Row(Modifier.fillMaxWidth()) {
                Text("DESCRIPTION", color = ReceiptMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("AMOUNT", color = ReceiptMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(p.title(), color = ReceiptInk, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Method: ${PayMethod.label(detail?.method)} · Ref: ${detail?.transactionRef ?: "—"}",
                        color = ReceiptMuted,
                        fontSize = 11.sp
                    )
                }
                Text(Fmt.usd(p.totalPaid), color = ReceiptInk, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Dashed()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("TOTAL PAID", color = ReceiptInk, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    Text(Fmt.khr(r.totalAmount), color = ReceiptMuted, fontSize = 11.sp)
                }
                Text("${Fmt.usd(r.totalAmount)} USD", color = RuppPalette.Forest800, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }

            Dashed()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE4E8E2), RoundedCornerShape(12.dp))
                        .padding(6.dp)
                ) {
                    QrCode("RUPP-RECEIPT|${r.receiptNumber}|payment=${p.id}|amount=${"%.2f".format(r.totalAmount)}", Modifier.fillMaxSize(), sizePx = 300)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("RUPP Official E-Receipt", color = ReceiptInk, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Verified by RUPP Finance Office", color = ReceiptMuted, fontSize = 11.sp)
                    Text("Issued ${Fmt.dateTime(r.issueDate)}", color = ReceiptMuted, fontSize = 11.sp)
                }
                PaidStamp()
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "This is a computer-generated receipt. No signature is required.",
                color = ReceiptMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Field(label: String, value: String, modifier: Modifier, end: Boolean = false, mono: Boolean = false) {
    Column(modifier, horizontalAlignment = if (end) Alignment.End else Alignment.Start) {
        Text(label, color = ReceiptMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(
            value,
            color = ReceiptInk,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = if (mono) FontFamily.Monospace else FontFamily.Default,
            textAlign = if (end) TextAlign.End else TextAlign.Start
        )
    }
}

@Composable
private fun Dashed() {
    Canvas(Modifier.fillMaxWidth().padding(vertical = 14.dp).height(1.dp)) {
        drawLine(
            color = Color(0xFFCAD2CC),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
        )
    }
}

/** Round red "RUPP FINANCE · PAID · VALIDATED" stamp. */
@Composable
private fun PaidStamp() {
    Box(
        Modifier
            .size(78.dp)
            .rotate(-14f)
            .border(2.dp, StampRed, CircleShape)
            .padding(4.dp)
            .border(1.dp, StampRed.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("RUPP FINANCE", color = StampRed, fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
            Text("PAID", color = StampRed, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text("VALIDATED", color = StampRed, fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}
