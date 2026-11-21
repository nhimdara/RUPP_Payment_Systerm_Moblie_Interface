package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.auth.AuthViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.cards.CampusCardsScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.ServiceFeeResponse
import androidx.compose.runtime.collectAsState
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.auth.LoginScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history.HistoryScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history.HistoryViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history.PaymentDetailScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.history.PaymentDetailViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.home.HomeScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.home.HomeViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.PayScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.pay.PayViewModel
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.profile.ProfileScreen
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.profile.ProfileViewModel

private enum class Gate { SPLASH, LOGIN, MAIN }

enum class Tab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Rounded.Home),
    PAY("Payments", Icons.Rounded.Payments),
    HISTORY("History", Icons.Rounded.History),
    PROFILE("Profile", Icons.Rounded.Person)
}

/**
 * Root of the redesigned student app (Figma flow): Splash -> Login -> tabs
 * (Home, Pay, History, Profile) with a payment detail screen on top.
 * All data comes from the RUPP backend through [RuppRepository].
 */
@Composable
fun RuppApp(repo: RuppRepository) {
    var gate by rememberSaveable { mutableStateOf(Gate.SPLASH) }
    // Bumped on every login so screen ViewModels are recreated for the new account.
    var sessionKey by rememberSaveable { mutableIntStateOf(0) }
    var loginMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        repo.session.sessionExpired.collect {
            if (gate == Gate.MAIN) {
                loginMessage = "Your session has expired. Please sign in again."
                gate = Gate.LOGIN
            }
        }
    }

    AnimatedContent(
        targetState = gate,
        transitionSpec = { fadeIn(tween(350)) togetherWith fadeOut(tween(250)) },
        label = "gate"
    ) { g ->
        when (g) {
            Gate.SPLASH -> Splash(repo) { loggedIn -> gate = if (loggedIn) Gate.MAIN else Gate.LOGIN }
            Gate.LOGIN -> {
                val vm: AuthViewModel = viewModel(key = "auth-$sessionKey") { AuthViewModel(repo) }
                LaunchedEffect(loginMessage) { loginMessage?.let { vm.showError(it); loginMessage = null } }
                LoginScreen(vm, onLoggedIn = { sessionKey++; gate = Gate.MAIN })
            }
            Gate.MAIN -> MainShell(repo, sessionKey, onLoggedOut = { sessionKey++; gate = Gate.LOGIN })
        }
    }
}

@Composable
private fun Splash(repo: RuppRepository, onResult: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        val start = System.currentTimeMillis()
        val loggedIn = if (!repo.session.hasSession) false else {
            when (val r = repo.me()) {
                is ApiResult.Success -> true
                // Server unreachable: keep the session and let screens show a retry state.
                is ApiResult.Failure -> r.httpCode == null && repo.session.hasSession
            }
        }
        val elapsed = System.currentTimeMillis() - start
        if (elapsed < 900) kotlinx.coroutines.delay(900 - elapsed)
        onResult(loggedIn)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Rupp.colors.hero),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(110.dp).clip(CircleShape).background(Color.White).padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.rupp_logo), contentDescription = "RUPP logo", modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.height(18.dp))
            Text("RUPP Pay", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text("Royal University of Phnom Penh", color = RuppPalette.Gold, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(28.dp))
            CircularProgressIndicator(color = RuppPalette.Green400, strokeWidth = 2.5.dp, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun MainShell(repo: RuppRepository, sessionKey: Int, onLoggedOut: () -> Unit) {
    var tab by rememberSaveable { mutableStateOf(Tab.HOME) }
    // Payment detail opened on top of the tabs (null = none).
    var openPaymentId by rememberSaveable { mutableStateOf<Long?>(null) }
    // Open the payment straight on its official receipt (from History > receipts).
    var openAsReceipt by rememberSaveable { mutableStateOf(false) }
    // RUPP Smart Campus Cards screen on top of the tabs.
    var showCards by rememberSaveable { mutableStateOf(false) }

    val home: HomeViewModel = viewModel(key = "home-$sessionKey") { HomeViewModel(repo) }
    val pay: PayViewModel = viewModel(key = "pay-$sessionKey") { PayViewModel(repo) }
    val history: HistoryViewModel = viewModel(key = "history-$sessionKey") { HistoryViewModel(repo) }
    val profile: ProfileViewModel = viewModel(key = "profile-$sessionKey") { ProfileViewModel(repo) }

    BackHandler(enabled = openPaymentId == null && !showCards && tab != Tab.HOME) { tab = Tab.HOME }

    val openPayment: (Long) -> Unit = { openAsReceipt = false; openPaymentId = it }
    val buyService: (ServiceFeeResponse) -> Unit = { fee -> showCards = false; pay.openService(fee); tab = Tab.PAY }

    val detailId = openPaymentId
    if (detailId != null) {
        val vm: PaymentDetailViewModel = viewModel(key = "payment-$detailId-$sessionKey") { PaymentDetailViewModel(repo, detailId) }
        val close = { openPaymentId = null; openAsReceipt = false; home.load(); history.refresh(); pay.load() }
        BackHandler { close() }
        PaymentDetailScreen(vm, onBack = close, openReceipt = openAsReceipt)
        return
    }

    val homeData = home.state.collectAsState().value.data
    if (showCards && homeData != null) {
        BackHandler { showCards = false }
        CampusCardsScreen(
            data = homeData,
            onBack = { showCards = false },
            onBuy = buyService,
            onOpenPayment = openPayment
        )
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
                Tab.entries.forEach { t ->
                    NavigationBarItem(
                        selected = tab == t,
                        onClick = {
                            if (t == Tab.HOME && tab != Tab.HOME) home.load()
                            if (t == Tab.HISTORY && tab != Tab.HISTORY) history.refresh()
                            if (t == Tab.PAY && tab != Tab.PAY) { if (pay.state.value.done) pay.reset() else pay.load() }
                            tab = t
                        },
                        icon = { Icon(t.icon, contentDescription = t.label) },
                        label = { Text(t.label, style = MaterialTheme.typography.labelMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (tab) {
                Tab.HOME -> HomeScreen(
                    vm = home,
                    onPay = { pay.openTuition(); tab = Tab.PAY },
                    onHistory = { tab = Tab.HISTORY },
                    onOpenPayment = openPayment,
                    onCards = { showCards = true },
                    onBuy = buyService
                )
                Tab.PAY -> PayScreen(
                    vm = pay,
                    onBackToHome = { home.load(); history.refresh(); tab = Tab.HOME },
                    onOpenPayment = openPayment
                )
                Tab.HISTORY -> HistoryScreen(
                    history,
                    onOpenPayment = openPayment,
                    onOpenReceipt = { openAsReceipt = true; openPaymentId = it }
                )
                Tab.PROFILE -> ProfileScreen(profile, onLoggedOut = onLoggedOut)
            }
        }
    }
}
