package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentReceipt
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptModal(
    receipt: PaymentReceipt,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val numberFormatter = remember { DecimalFormat("#,###") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF9F8F4),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.96f)
                .navigationBarsPadding()
        ) {
            // 1. Modal Top Bar: Check Icon + Title + Close Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD1FAE5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Official Electronic Receipt",
                            color = Color(0xFF1F2937),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = receipt.receiptNumber,
                            color = Color(0xFF6B7280),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 2. Paper Receipt Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    // Subtle Background Watermark
                    Image(
                        painter = painterResource(id = R.drawable.rupp_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.Center),
                        alpha = 0.05f
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // National Motto & Header
                        Text(
                            text = "KINGDOM OF CAMBODIA · NATION RELIGION KING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B7280),
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ព្រះរាជាណាចក្រកម្ពុជា ជាតិ សាសនា ព្រះមហាក្សត្រ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3F32),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Official Circular RUPP Logo
                        Image(
                            painter = painterResource(id = R.drawable.rupp_logo),
                            contentDescription = "RUPP Seal",
                            modifier = Modifier.size(52.dp)
                        )

                        Text(
                            text = "ROYAL UNIVERSITY OF PHNOM PENH",
                            color = Color(0xFF0F3B66),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ (RUPP)",
                            color = Color(0xFF1E3F32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Finance, Accounting & Student Tuition Department",
                            color = Color(0xFF9CA3AF),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )

                        ReceiptDashedDivider()

                        // Metadata Grid
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ReceiptLabelValue(
                                    label = "RECEIPT NUMBER",
                                    value = receipt.receiptNumber,
                                    isMonospace = true
                                )
                                ReceiptLabelValue(
                                    label = "PAYMENT DATE",
                                    value = receipt.issueDate,
                                    alignEnd = true
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ReceiptLabelValue(
                                    label = "STUDENT NAME",
                                    value = receipt.studentName
                                )
                                ReceiptLabelValue(
                                    label = "STUDENT ID",
                                    value = receipt.studentId,
                                    isMonospace = true,
                                    alignEnd = true
                                )
                            }

                            ReceiptLabelValue(
                                label = "FACULTY / DEPARTMENT",
                                value = receipt.facultyName
                            )
                        }

                        ReceiptDashedDivider()

                        // Description & Amount Table
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "DESCRIPTION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "AMOUNT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                    Text(
                                        text = receipt.feeTitle,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Method: ${receipt.paymentMethod.title} · Ref: ${receipt.transactionRef}",
                                        fontSize = 10.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }

                                Text(
                                    text = "$${String.format("%.2f", receipt.totalAmount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                            }
                        }

                        ReceiptDashedDivider()

                        // Total Paid Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TOTAL PAID",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "≈ ${numberFormatter.format(receipt.amountInKhr)} KHR",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }

                            Text(
                                text = "$${String.format("%.2f", receipt.totalAmount)} USD",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E3F32)
                            )
                        }

                        ReceiptDashedDivider()

                        // Footer: Official E-Receipt & Circular Rubber Stamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // QR Verification badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFAFAFA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = Color(0xFF1F2937),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "RUPP Official E-Receipt",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Text(
                                        text = "Verified by Treasury & NBC",
                                        fontSize = 9.sp,
                                        color = Color(0xFF9CA3AF)
                                    )
                                }
                            }

                            // Circular Red Rubber Stamp
                            ReceiptOfficialRedStamp()
                        }
                    }
                }

                // 3. Bottom Action Bar: Print / Save PDF, Share, Done
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Print / Save PDF Button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF374151)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Print / Save PDF",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Share Button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF374151)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Done Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E3F32),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Done",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptLabelValue(
    label: String,
    value: String,
    isMonospace: Boolean = false,
    alignEnd: Boolean = false
) {
    Column(horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Text(
            text = label,
            color = Color(0xFF9CA3AF),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = Color(0xFF1F2937),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default
        )
    }
}

@Composable
private fun ReceiptDashedDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
        drawLine(
            color = Color(0xFFE5E7EB),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}

@Composable
private fun ReceiptOfficialRedStamp() {
    Box(
        modifier = Modifier
            .rotate(-6f)
            .size(72.dp)
            .clip(CircleShape)
            .border(2.dp, Color(0xFFC62828), CircleShape)
            .padding(3.dp)
            .border(1.dp, Color(0xFFC62828).copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RUPP FINANCE",
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828),
                letterSpacing = 0.5.sp
            )
            Text(
                text = "PAID",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFC62828),
                letterSpacing = 1.sp
            )
            Text(
                text = "VALIDATED",
                fontSize = 6.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828),
                letterSpacing = 0.5.sp
            )
        }
    }
}
