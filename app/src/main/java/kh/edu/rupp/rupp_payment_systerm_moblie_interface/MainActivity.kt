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
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.SchoolFeeItem
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.HomeScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.SchoolFeeListScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.DarkBackground
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

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

/**
 * Simple navigation state to switch between:
 * 1. Home ("To pay" list)
 * 2. Fee Plan ("SchoolFeeListScreen")
 */
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("HOME") }
    var selectedFee by remember { mutableStateOf<SchoolFeeItem?>(null) }

    when (currentScreen) {
        "HOME" -> {
            HomeScreen(
                onFeePlanClick = {
                    currentScreen = "FEE_PLAN"
                },
                onFeeItemClick = { clickedFee ->
                    selectedFee = clickedFee
                    // In next step, this will open Screen 2: Payment Details!
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
                    // In next step, this will open Screen 2: Payment Details!
                }
            )
        }
    }
}
