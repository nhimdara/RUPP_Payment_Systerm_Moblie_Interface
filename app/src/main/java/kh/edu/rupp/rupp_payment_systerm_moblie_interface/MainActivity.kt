package kh.edu.rupp.rupp_payment_systerm_moblie_interface

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.AppContainer
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.app.RuppApp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppTheme

/**
 * Entry point. Hosts the redesigned, backend-driven student app ([RuppApp]).
 *
 * The earlier screens (LoginScreen, HomeScreen, MainAppNavigation, Smart Campus Cards, Top-up, etc.)
 * are still in ui/screens and ui/navigation but are no longer wired in, because they use sample data
 * and some have no backend API yet.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContainer.init(applicationContext)
        val repo = AppContainer.repository
        requestLocalNetworkAccess()

        setContent {
            val dark by repo.session.darkMode.collectAsState()
            RuppTheme(darkTheme = dark) {
                RuppApp(repo)
            }
        }
    }

    private val localNetworkPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* result read on next request */ }

    /**
     * Android 17 (API 37) drops connections to private-network addresses (the emulator's 10.0.2.2,
     * a PC at 192.168.x.x) until the user grants "local network" access, so the backend times out.
     */
    private fun requestLocalNetworkAccess() {
        if (Build.VERSION.SDK_INT < 37) return
        val granted = ContextCompat.checkSelfPermission(this, LOCAL_NETWORK) == PackageManager.PERMISSION_GRANTED
        if (!granted) localNetworkPermission.launch(LOCAL_NETWORK)
    }

    private companion object {
        const val LOCAL_NETWORK = "android.permission.ACCESS_LOCAL_NETWORK"
    }
}
