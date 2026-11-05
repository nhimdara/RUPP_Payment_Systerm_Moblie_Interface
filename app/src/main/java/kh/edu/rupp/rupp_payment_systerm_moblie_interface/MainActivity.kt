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
    // 1. Track which screen is currently open
    var currentScreen by remember { mutableStateOf("HOME") }

    // 2. Track which fee the student tapped
    var selectedFee by remember { mutableStateOf<SchoolFeeItem?>(null) }

    // 3. Flag to show or hide the PaymentModal bottom sheet
    var showPaymentModal by remember { mutableStateOf(false) }

    // 4. Navigate between HOME and FEE_PLAN screens
    when (currentScreen) {
        "HOME" -> {
            HomeScreen(
                onFeePlanClick = {
                    currentScreen = "FEE_PLAN"
                },
                onPayTuitionClick = {
                    // "Pay tuition" shortcut: same as tapping the tuition fee card
                    currentScreen = "FEE_PLAN"
                },
                onFeeItemClick = { clickedFee ->
                    selectedFee = clickedFee       // Save the fee the student tapped
                    showPaymentModal = true         // Show the payment modal!
                }
            )
        }

        "FEE_PLAN" -> {
            SchoolFeeListScreen(
                onNavigateBack = {
                    currentScreen = "HOME"
                },
                onFeeClick = { clickedFee ->
                    selectedFee = clickedFee       // Save the fee the student tapped
                    showPaymentModal = true         // Show the payment modal!
                }
            )
        }
    }

    // 5. Show the PaymentModal on top of any screen when triggered
    if (showPaymentModal && selectedFee != null) {
        PaymentModal(
            feeItem = selectedFee!!,
            studentBalance = 120.00,
            onDismiss = {
                // Student closed the sheet → hide it
                showPaymentModal = false
            },
            onConfirmPayment = { fee, method ->
                // Student confirmed payment!
                // For now: close the modal
                // Next step: open the Payment Status / Receipt screen!
                showPaymentModal = false
            }
        )
    }
}
