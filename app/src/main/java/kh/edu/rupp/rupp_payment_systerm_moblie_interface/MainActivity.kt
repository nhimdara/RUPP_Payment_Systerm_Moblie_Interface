package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentMethodType
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.PaymentReceipt
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.PaymentModal
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.components.ReceiptModal
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.HomeScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.SchoolFeeListScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    // 1. Navigation state
    var currentScreen by remember { mutableStateOf("HOME") }
    var selectedFee by remember { mutableStateOf<SchoolFeeItem?>(null) }
    var showPaymentModal by remember { mutableStateOf(false) }

    // 2. Active Electronic Receipt state
    var activeReceipt by remember { mutableStateOf<PaymentReceipt?>(null) }

    // 3. Screen Switcher
    when (currentScreen) {
        "HOME" -> {
            HomeScreen(
                onFeePlanClick = {
                    currentScreen = "FEE_PLAN"
                },
                onPayTuitionClick = {
                    currentScreen = "FEE_PLAN"
                },
                onFeeItemClick = { clickedFee ->
                    selectedFee = clickedFee
                    showPaymentModal = true
                }
            )
        }

        "FEE_PLAN" -> {
            SchoolFeeListScreen(
                onNavigateBack = {
                    currentScreen = "HOME"
                },
                onFeeClick = { clickedFee ->
                    selectedFee = clickedFee
                    showPaymentModal = true
                }
            )
        }
    }

    // 4. Payment Checkout Modal
    if (showPaymentModal && selectedFee != null) {
        PaymentModal(
            feeItem = selectedFee!!,
            studentBalance = 120.00,
            onDismiss = {
                showPaymentModal = false
            },
            onConfirmPayment = { fee, method ->
                // Hide checkout modal
                showPaymentModal = false

                // Generate official RUPP electronic receipt
                val timestamp = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
                val randomReceiptId = (1000..9999).random()

                activeReceipt = PaymentReceipt(
                    receiptNumber = "RCP-2026-$randomReceiptId",
                    studentName = "Sokha Chan",
                    studentId = "20260142",
                    facultyName = "Faculty of Science",
                    majorName = "Computer Science & Engineering",
                    feeTitle = fee.title,
                    totalAmount = fee.totalAmount,
                    paymentMethod = method,
                    transactionRef = "TXN-${System.currentTimeMillis().toString().takeLast(8)}",
                    issueDate = timestamp
                )
            }
        )
    }

    // 5. Official Electronic Receipt Modal (pops up right after payment is confirmed!)
    activeReceipt?.let { receipt ->
        ReceiptModal(
            receipt = receipt,
            onDismiss = {
                activeReceipt = null
            }
        )
    }
}
