package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import java.text.DecimalFormat

enum class TopUpDepositMethod(val title: String, val subtitle: String, val badge: String, val color: Color) {
    BAKONG("Bakong", "Any Khmer Bank", "KHQR", Color(0xFFDE3618)),
    ABA("ABA Pay", "Instant Push", "ABA", Color(0xFF096894)),
    WING("Wing Bank", "Wallet / App", "WING", Color(0xFF059669))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpModal(
    currentBalance: Double = 145.00,
    onDismiss: () -> Unit,
    onTopUpConfirmed: (Double) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var selectedAmount by remember { mutableDoubleStateOf(50.0) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf(TopUpDepositMethod.BAKONG) }

    val presetAmounts = listOf(10.0, 20.0, 50.0, 100.0, 200.0)
    val numberFormatter = remember { DecimalFormat("#,###") }
    val khrAmount = (selectedAmount * 4100).toLong()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFFCFBF9),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8ECE9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rupp_logo),
                            contentDescription = "RUPP Seal",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Top Up Student Balance",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3F32)
                        )
                        Text(
                            text = "Current balance: $${String.format("%.2f", currentBalance)}",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
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
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // SELECT AMOUNT Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "SELECT AMOUNT (USD)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetAmounts.forEach { amount ->
                        val isSelected = selectedAmount == amount && customAmountText.isEmpty()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF1E3F32) else Color.White)
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF1E3F32) else Color(0xFFE5E7EB),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedAmount = amount
                                    customAmountText = ""
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$${amount.toInt()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF1F2937)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { input ->
                        val sanitized = input.filter { it.isDigit() || it == '.' }
                        customAmountText = sanitized
                        val parsed = sanitized.toDoubleOrNull()
                        if (parsed != null && parsed > 0) {
                            selectedAmount = parsed
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "$ Or enter custom amount",
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF1E3F32),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )
            }

            // DEPOSIT METHOD Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "DEPOSIT METHOD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TopUpDepositMethod.values().forEach { method ->
                        val isSelected = selectedMethod == method
                        val borderColor = if (isSelected) method.color else Color(0xFFE5E7EB)
                        val borderWidth = if (isSelected) 2.dp else 1.dp

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
                                .clickable { selectedMethod = method }
                                .padding(vertical = 14.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(method.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = method.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                Text(
                                    text = method.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )

                                Text(
                                    text = method.subtitle,
                                    fontSize = 10.sp,
                                    color = Color(0xFF6B7280),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // High Fidelity KHQR Interactive QR Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = Color(0xFFDE3618),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "KHQR Individual",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDE3618)
                            )
                        }

                        Text(
                            text = "RUPP Finance Acct #2026",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    // QR Visual with Center KHQR Logo
                    Box(
                        modifier = Modifier
                            .size(165.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFAFAFA))
                            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QrCornerPattern()
                                QrCornerPattern()
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QrCornerPattern()
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(Color(0xFF1E3F32), RoundedCornerShape(4.dp))
                                )
                            }
                        }

                        // KHQR Center Badge
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDE3618)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "KHQR",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Scan with Bakong or any Cambodian Banking App",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Acct Name: RUPP TREASURY (SOKHA CHAN)",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280)
                        )
                        Text(
                            text = "Amount: $${String.format("%.2f", selectedAmount)} USD (≈ ${numberFormatter.format(khrAmount)} KHR)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3F32)
                        )
                    }
                }
            }

            // Bottom Action: Simulate Pay Button
            Button(
                onClick = {
                    onTopUpConfirmed(selectedAmount)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E3F32),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Simulate Pay $${String.format("%.2f", selectedAmount)} ->",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QrCornerPattern() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(Color(0xFF111827), RoundedCornerShape(6.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(Color.White, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFF111827), RoundedCornerShape(2.dp))
            )
        }
    }
}

