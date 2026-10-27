package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.PaymentHistoryScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RUPP_Payment_Systerm_Moblie_InterfaceTheme(darkTheme = true) {
                PaymentHistoryScreen()
            }
        }
    }
}

