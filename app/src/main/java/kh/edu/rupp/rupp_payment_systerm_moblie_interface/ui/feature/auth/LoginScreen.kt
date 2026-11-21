package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.R
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.ErrorBanner
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.GradientButton
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.IconBadge
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.Rupp
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppCard
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.design.RuppPalette

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit) {
    val state by vm.state.collectAsState()
    val focus = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }
    var showServer by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Gradient backdrop with slowly drifting light orbs
        Box(
            Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(Rupp.colors.hero)
        ) {
            // RUPP Campus 1 under a green wash
            Image(
                painterResource(R.drawable.rupp_campus),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                RuppPalette.Forest900.copy(alpha = 0.78f),
                                RuppPalette.Forest800.copy(alpha = 0.86f),
                                RuppPalette.Forest700.copy(alpha = 0.96f)
                            )
                        )
                    )
            )
            FloatingOrbs()
        }

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { showServer = true }) {
                    Icon(Icons.Rounded.Dns, contentDescription = "Server settings", tint = Color.White.copy(alpha = 0.8f))
                }
            }

            Box(
                Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painterResource(R.drawable.rupp_logo), contentDescription = "RUPP logo", modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.height(14.dp))
            Text("RUPP FINTECH SERVICES", color = RuppPalette.Gold, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                "Royal University of Phnom Penh",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                "សាកលវិទ្យាល័យភូមិន្ទភ្នំពេញ · Student Finance Portal",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            RuppCard(Modifier.fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBadge(Icons.Rounded.Shield, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Student Identity Gateway", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Sign in with your RUPP account to manage tuition and receipts",
                            style = MaterialTheme.typography.bodySmall,
                            color = Rupp.colors.muted
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = state.login,
                    onValueChange = vm::onLoginChange,
                    label = { Text("Username or university email") },
                    leadingIcon = { Icon(Icons.Rounded.Badge, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = vm::onPasswordChange,
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Rounded.Lock, null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focus.clearFocus(); vm.submit(onLoggedIn) }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = state.remember,
                        onCheckedChange = vm::onRememberChange,
                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Remember my username", style = MaterialTheme.typography.bodyMedium)
                }

                AnimatedVisibility(state.error != null) {
                    ErrorBanner(state.error.orEmpty(), Modifier.padding(top = 10.dp))
                }
                Spacer(Modifier.height(16.dp))
                GradientButton(
                    text = "Sign in to Portal",
                    icon = Icons.AutoMirrored.Rounded.Login,
                    loading = state.loading,
                    onClick = { focus.clearFocus(); vm.submit(onLoggedIn) }
                )
            }

            Spacer(Modifier.height(20.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Rupp.colors.goldBg)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔒", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(10.dp))
                Text(
                    "Your session is protected with JWT tokens, and every payment is confirmed with a one-time code.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Rupp.colors.gold
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Need access? Contact the Faculty IT Desk or Academic Office.",
                style = MaterialTheme.typography.bodySmall,
                color = Rupp.colors.muted,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showServer) {
        AlertDialog(
            onDismissRequest = { showServer = false },
            title = { Text("Server address") },
            text = {
                Column {
                    Text(
                        "Emulator: http://10.0.2.2:8081/\nReal phone: http://<your-PC-LAN-IP>:8081/",
                        style = MaterialTheme.typography.bodySmall,
                        color = Rupp.colors.muted
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.serverUrl,
                        onValueChange = vm::onServerUrlChange,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.serverCheck?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (it.startsWith("Connected")) Rupp.colors.success else Rupp.colors.danger
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = vm::saveAndTestServer, enabled = !state.checkingServer) {
                    Text(if (state.checkingServer) "Checking…" else "Save & test")
                }
            },
            dismissButton = { TextButton(onClick = { showServer = false }) { Text("Close") } }
        )
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    unfocusedBorderColor = Rupp.colors.cardBorder
)

@Composable
private fun FloatingOrbs() {
    val t = rememberInfiniteTransition(label = "orbs")
    val a by t.animateFloat(0f, 1f, infiniteRepeatable(tween(6000), RepeatMode.Reverse), label = "a")
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .offset(x = (-40 + 30 * a).dp, y = (40 - 20 * a).dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(RuppPalette.Green400.copy(alpha = 0.16f))
        )
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = (50 - 30 * a).dp, y = (-30 + 40 * a).dp)
                .size(200.dp)
                .clip(CircleShape)
                .background(RuppPalette.Gold.copy(alpha = 0.10f))
        )
    }
}
