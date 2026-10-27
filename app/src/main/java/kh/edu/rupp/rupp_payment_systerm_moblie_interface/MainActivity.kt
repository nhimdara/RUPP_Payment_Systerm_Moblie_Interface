package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.LoginScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RUPP_Payment_Systerm_Moblie_InterfaceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        onLoginSuccess = { request ->
                            Toast.makeText(this, "Logged in as ${request.usernameOrEmail}", Toast.LENGTH_SHORT).show()
                        },
                        onForgotPasswordClick = {
                            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
                        },
                        onBiometricClick = {
                            Toast.makeText(this, "Biometric login triggered", Toast.LENGTH_SHORT).show()
                        },
                        onContactSupportClick = {
                            Toast.makeText(this, "Opening RUPP ICT Helpdesk", Toast.LENGTH_SHORT).show()
                        },
                        onRegisterClick = {
                            Toast.makeText(this, "Account activation clicked", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
