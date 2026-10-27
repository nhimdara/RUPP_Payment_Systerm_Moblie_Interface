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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.ForgotPasswordScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.LoadingScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.LoginScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.screens.RegisterScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.theme.RUPP_Payment_Systerm_Moblie_InterfaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RUPP_Payment_Systerm_Moblie_InterfaceTheme {
                var currentScreen by remember { mutableStateOf("loading") }

                BackHandler(enabled = currentScreen != "login" && currentScreen != "loading") {
                    currentScreen = "login"
                }

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
                }
            }
        }
    }
}
