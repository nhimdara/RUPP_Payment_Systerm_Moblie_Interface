package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composableimport androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.ForgotPasswordScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.LoadingScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.LoginScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.RegisterScreen
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.PaymentHistoryScreenimport kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.navigation.MainAppNavigation
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
            RUPP_Payment_Systerm_Moblie_InterfaceTheme {
                var currentScreen by remember { mutableStateOf("loading") }

                BackHandler(enabled = currentScreen != "login" && currentScreen != "loading") {
                    currentScreen = "login"
        RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    AppNavigation()                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            "loading" -> {
                                LoadingScreen(
                                    onLoadingComplete = {
                                        currentScreen = "login"
                                    }
                                )
                            }
                            "login" -> {
                                LoginScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onLoginSuccess = { request ->
                                        Toast.makeText(this, "Logged in as ${request.usernameOrEmail}", Toast.LENGTH_SHORT).show()
                                    },
                                    onRegisterClick = {
                                        currentScreen = "register"
                                    },
                                    onForgotPasswordClick = {
                                        currentScreen = "forgot_password"
                                    },
                                    onBiometricClick = {
                                        Toast.makeText(this, "Biometric login triggered", Toast.LENGTH_SHORT).show()
                                    },
                                    onContactSupportClick = {
                                        Toast.makeText(this, "Opening RUPP ICT Helpdesk", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            "register" -> {
                                RegisterScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onRegisterSuccess = { request ->
                                        Toast.makeText(this, "Account created for ${request.username}!", Toast.LENGTH_LONG).show()
                                        currentScreen = "login"
                                    },
                                    onNavigateToLogin = {
                                        currentScreen = "login"
                                    },
                                    onContactSupportClick = {
                                        Toast.makeText(this, "Opening RUPP ICT Helpdesk", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            "forgot_password" -> {
                                ForgotPasswordScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    onNavigateBack = {
                                        currentScreen = "login"
                                    },
                                    onResetComplete = {
                                        Toast.makeText(this, "Password reset successfully! Please sign in.", Toast.LENGTH_LONG).show()
                                        currentScreen = "login"
                                    },
                                    onContactSupportClick = {
                                        Toast.makeText(this, "Opening RUPP ICT Helpdesk", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                       RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppNavigation()
                }
                            RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
                PaymentHistoryScreen()

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
